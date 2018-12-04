fun main() {
    val fin = java.io.File("data/02.txt").inputStream()
    val lines = fin.bufferedReader().useLines { it.toList() }
    var numTwos = 0
    var numThrees = 0
    for (line in lines) {
        val f = line.groupingBy{it}.eachCount()
        if (f.values.contains(2)) {
            numTwos += 1
        }
        if (f.values.contains(3)) {
            numThrees += 1
        }
    }
    println("part 1: ${numTwos * numThrees}")

    lines.map {line ->
        (0 until line.length).map { i ->
            Pair(i, line.slice(0 until i) + line.slice(i + 1 until line.length))
        }
    }.flatten().groupingBy{it}.eachCount().forEach { p, cnt ->
        if (cnt == 2) {
            println("part 2: ${p.second}")
        }
    }
}
