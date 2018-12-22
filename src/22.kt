const val depth = 3066
const val xTarget = 13
const val yTarget = 726

const val M = 20183

fun main() {
    val erosion = List(yTarget + 1) { MutableList(xTarget + 1) { 0 }}
    for ((y, row) in erosion.withIndex()) {
        for (x in 0 until row.size) {
            row[x] = (when {
                x == 0 && y == 0 -> 0
                x == xTarget && y == yTarget -> 0
                y == 0 -> x * 16807
                x == 0 -> y * 48271
                else -> erosion[y][x - 1] * erosion[y - 1][x]
            } + depth) % M
        }
    }
    println("part 1: ${erosion.flatten().sumBy { it % 3}}")
}
