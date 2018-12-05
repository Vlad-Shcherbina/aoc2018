fun main() {
    val lines = java.io.File("data/03.txt").inputStream().bufferedReader().useLines{it.toList()}
    val re = kotlin.text.Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

    val overlapCnt = lines.asSequence().flatMap{ line ->
        val m = re.matchEntire(line)!!
        val left = m.groups[2]!!.value.toInt()
        val top = m.groups[3]!!.value.toInt()
        val width = m.groups[4]!!.value.toInt()
        val height = m.groups[5]!!.value.toInt()
        (left until left + width).asSequence().flatMap { x ->
            (top until top + height).asSequence().map { y -> Pair(x, y) }
        }
    }.groupingBy { it }.eachCount().filter { it.value > 1 }.count()
    println("part1: $overlapCnt")
}
