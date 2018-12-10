fun<T> MutableList<T>.popBack(): T {
    val result = this[size - 1]
    removeAt(size - 1)
    return result
}

class CircularTape<T>(var focus: T) {
    private var left = mutableListOf<T>()
    private var right = mutableListOf<T>()

    override fun toString() = "$left ($focus) ${right.asReversed()}"

    fun insertRight(x: T) {
        left.add(focus)
        focus = x
    }

    fun moveRight(dist: Int = 1) {
        for (i in 1..dist) {
            left.add(focus)
            if (right.isEmpty()) {
                val t = (left.size + 1) / 2
                right = left.slice(0 until t).asReversed().toMutableList()
                left = left.slice(t until left.size).toMutableList()
            }
            focus = right.popBack()
        }
    }

    fun moveLeft(dist: Int = 1) {
        val t = left; left = right; right = t
        moveRight(dist)
        val t2 = left; left = right; right = t2
    }

    fun removeFocusAndMoveRight() {
        if (right.isEmpty()) {
            val t = (left.size + 1) / 2
            right = left.slice(0 until t).asReversed().toMutableList()
            left = left.slice(t until left.size).toMutableList()
        }
        focus = right.popBack()
    }
}

fun solve(numPlayers: Int, lastMarble: Int): Long {
    val scores = (1..numPlayers).map { 0L }.toMutableList()
    val tape = CircularTape(0)
    for (marble in 1..lastMarble) {
        if(marble % 23 != 0) {
            tape.moveRight()
            tape.insertRight(marble)
        } else {
            scores[marble % numPlayers] += marble.toLong()
            tape.moveLeft(7)
            scores[marble % numPlayers] += tape.focus.toLong()
            tape.removeFocusAndMoveRight()
        }
    }
    return scores.max()!!
}

fun main() {
    val t = kotlin.system.measureTimeMillis {
        println("part 1: ${solve(452, 71250)}")
        println("part 2: ${solve(452, 7125000)}")
    }
    println("it took $t ms")
}
