fun main() {
    val fin = java.io.File("data/01.txt").inputStream()
    val deltas = fin.bufferedReader().useLines {
        it.map { line -> line.toInt() }.toList()
    }
    println("part 1: ${deltas.sum()}")
    var s = 0
    val seen = mutableSetOf<Int>()
    outer@ while (true) {
        for (delta in deltas) {
            s += delta
            if (!seen.add(s)) {
                println("part 2: $s")
                break@outer
            }
        }
    }
}
