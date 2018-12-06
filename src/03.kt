fun main() {
    val lines = java.io.File("data/03.txt").inputStream().bufferedReader().useLines{it.toList()}
    val re = kotlin.text.Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

    val idsByPt = lines.asSequence().flatMap { line ->
        val m = re.matchEntire(line)!!
        val (idStr, leftStr, topStr, widthStr, heightStr) = m.destructured
        val id = idStr.toInt()
        val left = leftStr.toInt()
        val top = topStr.toInt()
        val width = widthStr.toInt()
        val height = heightStr.toInt()
        (left until left + width).asSequence().flatMap { x ->
            (top until top + height).asSequence().map { y -> Pair(Pair(x, y), id) }
        }
    }.groupBy { it.first }.mapValues { kv -> kv.value.map { it.second } }

    println("part 1: ${idsByPt.filter { it.value.size > 1 }.count()}")

    val overlappingIds = idsByPt.values.asSequence().flatMap { ids ->
        if (ids.size > 1) {
            ids.asSequence()
        } else {
            emptySequence()
        }
    }.toSet()
    val allIds = idsByPt.values.asSequence().flatMap { it.asSequence() }.toSet()
    println("part 2: ${allIds - overlappingIds}")
}
