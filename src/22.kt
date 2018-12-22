const val depth = 3066
const val xTarget = 13
const val yTarget = 726

const val M = 20183

fun main() {
    val erosion = mutableListOf<MutableList<Int>>()
    fun getType(x0: Int, y0: Int): Int {
        (erosion.size .. y0).forEach { erosion.add(mutableListOf() )}
        val yStart = (0 .. y0).reversed().find { x0 < erosion[it].size }?: -1
        for (y in yStart + 1 .. y0) {
            assert(x0 >= erosion[y].size) { "$x0, $y, $erosion" }
            for (x in erosion[y].size .. x0) {
                erosion[y].add((when {
                    x == 0 && y == 0 -> 0
                    x == xTarget && y == yTarget -> 0
                    y == 0 -> x * 16807
                    x == 0 -> y * 48271
                    else -> erosion[y][x - 1] * erosion[y - 1][x]
                } + depth) % M)
            }
        }
        return erosion[y0][x0] % 3
    }
    var s = 0
    for (y in 0..yTarget) {
        for (x in 0..xTarget) {
            s += getType(x, y)
        }
    }
    println("part 1: $s")

    // neither - 0
    // torch - 1
    // climbing gear - 2
    data class State(val x: Int, val y: Int, val equipment: Int)

    fun allowed(s: State): Boolean {
        return s.x >= 0 &&
                s.y >= 0 &&
                s.equipment != getType(s.x, s.y)
    }
    val finish = State(xTarget, yTarget, 1)
    val start = State(0, 0, 1)

    val visited = mutableSetOf<State>()
    val q = MutableList(8) { mutableListOf<State>() }
    q[0].add(start)
    var t = 0
    outer@while (q.any { !it.isEmpty() }) {
        for (u in q[0]) {
            if (u == finish) {
                println("part 2: $t")
                break@outer
            }
            if (visited.contains(u)) {
                continue
            }
            visited.add(u)
            val es = listOf(
                Pair(State(u.x - 1, u.y, u.equipment), 1),
                Pair(State(u.x + 1, u.y, u.equipment), 1),
                Pair(State(u.x, u.y - 1, u.equipment), 1),
                Pair(State(u.x, u.y + 1, u.equipment), 1),
                Pair(State(u.x, u.y, 0), 7),
                Pair(State(u.x, u.y, 1), 7),
                Pair(State(u.x, u.y, 2), 7)
            )
            for ((v, d) in es) {
                if (allowed(v) && !visited.contains(v)) {
                    q[d].add(v)
                }
            }
        }
        t++
        q.removeAt(0)
        q.add(mutableListOf())
    }
}
