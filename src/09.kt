fun solve(numPlayers: Int, lastMarble: Int): Long {
    val tape = java.util.ArrayDeque<Int>()
    tape.addLast(0)
    val scores = (1..numPlayers).map { 0L }.toMutableList()
    for (marble in 1..lastMarble) {
        if(marble % 23 != 0) {
            tape.addLast(tape.removeFirst())
            tape.addLast(marble)
        } else {
            scores[marble % numPlayers] += marble.toLong()
            for (i in 1..7) {
                tape.addFirst(tape.removeLast())
            }
            scores[marble % numPlayers] += tape.removeLast().toLong()
            tape.addLast(tape.removeFirst())
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
