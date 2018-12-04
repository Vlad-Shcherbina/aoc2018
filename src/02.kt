fun <T> freqs(xs: Iterable<T>): Map<T, Int> {
    val res = mutableMapOf<T, Int>()
    for (x in xs) {
        res[x] = (res[x] ?: 0) + 1
    }
    return res
}

fun main() {
    val fin = java.io.File("data/02.txt").inputStream()
    val lines = fin.bufferedReader().useLines { it.toList() }
    var numTwos = 0
    var numThrees = 0
    for (line in lines) {
        val f = freqs(line.asIterable())
        if (f.values.contains(2)) {
            numTwos += 1
        }
        if (f.values.contains(3)) {
            numThrees += 1
        }
    }
    println("part 1: ${numTwos * numThrees}")
}
