fun main() {
    val fin = java.io.File("data/01.txt").inputStream()
    val text = fin.bufferedReader().readText()
    fin.close()
    val deltas = text.trimEnd().lines().map { line -> line.toInt() }
    println("part 1: ${deltas.sum()}")
    var s = 0
    var seen = mutableSetOf<Int>()
    var cnt = 0
    outer@ while (true) {
        for (delta in deltas) {
            s += delta
            cnt++
            if (seen.contains(s)) {
                println("part 2: $s")
                break@outer
            }
            seen.add(s)
        }
    }
}
