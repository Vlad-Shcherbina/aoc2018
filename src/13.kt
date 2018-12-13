import Turn.LEFT
import Turn.STRAIGHT
import Turn.RIGHT

enum class Turn { LEFT, STRAIGHT, RIGHT}
data class Cart(var x: Int, var y: Int, var vx: Int, var vy: Int, var turn: Turn, var alive: Boolean = true)

fun main() {
    val lines = java.io.File("data/13.txt").inputStream().bufferedReader().useLines { it.toList() }
    val map = lines.map { it.toCharArray().toMutableList() }
    val carts = mutableListOf<Cart>()
    for ((i, line) in map.withIndex()) {
        for (j in 0 until line.size) {
            when (line[j]) {
                'v' -> {
                    carts.add(Cart(x=j, y=i, vx=0, vy=1, turn= LEFT))
                    line[j] = '|'
                }
                '^' -> {
                    carts.add(Cart(x=j, y=i, vx=0, vy=-1, turn= LEFT))
                    line[j] = '|'
                }
                '>' -> {
                    carts.add(Cart(x=j, y=i, vx=1, vy=0, turn= LEFT))
                    line[j] = '-'
                }
                '<' -> {
                    carts.add(Cart(x=j, y=i, vx=-1, vy=0, turn= LEFT))
                    line[j] = '-'
                }
                '/', '\\', '-', '|', '+', ' ' -> {}
                else -> { println(line[j]) }
            }
        }
    }

    var firstCollision = true
    while (true) {
        carts.sortWith(compareBy({ it.y }, { it.x }))
        for ((i, cart) in carts.withIndex()) {
            if (!cart.alive) {
                continue
            }
            cart.x += cart.vx
            cart.y += cart.vy
            val other = carts.withIndex().find {
                it.index != i &&
                it.value.alive &&
                it.value.x == cart.x &&
                it.value.y == cart.y }
            if (other != null) {
                cart.alive = false
                other.value.alive = false
                if (firstCollision) {
                    println("part 1: ${cart.x},${cart.y}")
                    firstCollision = false
                }
            }
            when (val c = map[cart.y][cart.x]) {
                '-', '|' -> {}
                '\\' -> {
                    val t = cart.vx; cart.vx = cart.vy; cart.vy = t
                }
                '/' -> {
                    val t = cart.vx; cart.vx = -cart.vy; cart.vy = -t
                }
                '+' -> when (cart.turn) {
                    LEFT -> {
                        val t = cart.vx; cart.vx = cart.vy; cart.vy = -t
                        cart.turn = STRAIGHT
                    }
                    STRAIGHT -> cart.turn = RIGHT
                    RIGHT -> {
                        val t = cart.vx; cart.vx = -cart.vy; cart.vy = t
                        cart.turn = LEFT
                    }
                }
                else -> {
                    assert(false) { "$cart $c" }
                }
            }
        }
        val c = carts.singleOrNull { it.alive }
        if (c != null) {
            println("part 2: ${c.x},${c.y}")
            break
        }
    }
}
