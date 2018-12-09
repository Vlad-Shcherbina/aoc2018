const val numPlayers = 452
const val lastMarble = 71250

data class Node(val value: Int, var cw: Node?, var ccw: Node?)

fun main() {
    var cur = Node(0, null, null)
    cur.cw = cur
    cur.ccw =cur
    val scores = (1..numPlayers).map { 0 }.toMutableList()
    for (marble in 1..lastMarble) {
        if (marble % 23 != 0) {
            cur = cur.cw!!
            val new = Node(marble, cur.cw, cur)
            new.cw!!.ccw = new
            new.ccw!!.cw = new
            cur = new
        } else {
            scores[marble % numPlayers] += marble
            for (i in 1..7) cur = cur.ccw!!
            scores[marble % numPlayers] += cur.value
            cur.ccw!!.cw = cur.cw
            cur.cw!!.ccw = cur.ccw
            cur = cur.cw!!
        }
    }
    println("part 1: ${scores.max()}")
}
