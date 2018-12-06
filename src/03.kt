fun main() {
    val lines = java.io.File("data/03.txt").inputStream().bufferedReader().useLines{it.toList()}
    val re = kotlin.text.Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

    val idsByPt = lines.asSequence().flatMap { line ->
        val m = re.matchEntire(line)!!
        val id = m.groups[1]!!.value.toInt()
        val left = m.groups[2]!!.value.toInt()
        val top = m.groups[3]!!.value.toInt()
        val width = m.groups[4]!!.value.toInt()
        val height = m.groups[5]!!.value.toInt()
        (left until left + width).asSequence().flatMap { x ->
            (top until top + height).asSequence().map { y -> Pair(Pair(x, y), id) }
        }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    println("part 1: ${idsByPt.filter { it.value.size > 1 }.count()}")

    val overlappingIds = idsByPt.asSequence().flatMap { kv ->
        val s = kv.value.toSet()
        if (s.size > 1) {
            s
        } else {
            emptySet()
        }.asSequence()
    }.toSet()
    val allIds = idsByPt.asSequence().flatMap { it.value.asSequence() }.toSet()
    println("part 2: ${allIds - overlappingIds}")
}
