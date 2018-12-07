import kotlin.math.abs

fun main() {
    val pts = java.io.File("data/06.txt").inputStream().bufferedReader().useLines { lines ->
        lines.map { line ->
            val (x, y) = line.split(", ").map { it.toInt() }
            Pair(x, y)
        }.toList()
    }

    val xMin = pts.map { it.first }.min()!!
    val xMax = pts.map { it.first }.max()!!
    val yMin = pts.map { it.second }.min()!!
    val yMax = pts.map { it.second }.max()!!

    val infinite = mutableSetOf<Pair<Int, Int>>()
    val areas = mutableMapOf<Pair<Int, Int>, Int>()
    for (x in xMin..xMax) {
        for (y in yMin..yMax) {
            val dist = { pt: Pair<Int, Int> -> abs(x - pt.first) + abs(y - pt.second) }
            val nearest1 = pts.minBy(dist)!!
            val nearest2 = pts.asReversed().minBy(dist)!!
            if (nearest1 != nearest2) continue
            if (x == xMin || x == xMax || y == yMin || y == yMax) {
                infinite.add(nearest1)
            }
            areas.merge(nearest1, 1) { a, b -> a + b}
        }
    }
    println("part 1: ${areas.filterKeys { !infinite.contains(it) }.values.max()}")

    val n = 10000
    val xd = numPointsByDistance(pts.map { it.first }, n)
    val yd = numPointsByDistance(pts.map { it.second }, n)
    val a = xd.map { kvx ->
        yd.filter { kvx.key + it.key <= n }
            .map { kvx.value * it.value }
            .sum()
    }.sum()
    println("part 2: $a")
}

fun numPointsByDistance(xs: Iterable<Int>, maxDistance: Int): Map<Int, Int> {
    return (xs.min()!! - maxDistance .. xs.max()!! + maxDistance)
        .map { x -> xs.sumBy { abs(x - it) } }
        .filter { it <= maxDistance }
        .groupingBy { it }
        .eachCount()
}
