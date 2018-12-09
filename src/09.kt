data class Node(val value: Int, var cw: Node?, var ccw: Node?)

fun solve(numPlayers: Int, lastMarble: Int): Long {
    var cur = Node(0, null, null)
    cur.cw = cur
    cur.ccw =cur
    val scores = (1..numPlayers).map { 0L }.toMutableList()
    for (marble in 1..lastMarble) {
        if (marble % 23 != 0) {
            cur = cur.cw!!
            val new = Node(marble, cur.cw, cur)
            new.cw!!.ccw = new
            new.ccw!!.cw = new
            cur = new
        } else {
            scores[marble % numPlayers] += marble.toLong()
            for (i in 1..7) cur = cur.ccw!!
            scores[marble % numPlayers] += cur.value.toLong()
            cur.ccw!!.cw = cur.cw
            cur.cw!!.ccw = cur.ccw
            cur = cur.cw!!
        }
    }
    return scores.max()!!
}

fun main() {
    println("part 1: ${solve(452, 71250)}")
    println("part 2: ${solve(452, 7125000)}")
}
