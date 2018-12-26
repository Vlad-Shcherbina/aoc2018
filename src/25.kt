import kotlin.math.abs

class DSU(n: Int) {
    val parent = MutableList(n) { it }
    val rank = MutableList(n) { 0 }

    fun getRoot(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = getRoot(parent[x])
        }
        return parent[x]
    }

    fun merge(x: Int, y: Int) {
        val xRoot = getRoot(x)
        val yRoot = getRoot(y)
        if (xRoot != yRoot) {
            when {
                rank[xRoot] < rank[yRoot] -> parent[xRoot] = yRoot
                rank[xRoot] > rank[yRoot] -> parent[yRoot] = xRoot
                else -> {
                    parent[yRoot] = xRoot
                    rank[xRoot]++
                }
            }
        }
    }
}

fun main() {
    val points = java.io.File("data/25.txt").bufferedReader().useLines { lines ->
        lines.map { line ->
            line.split(",").map { it.toInt() }
        }.toList()
    }
    val dsu = DSU(points.size)
    for ((i, pt1) in points.withIndex()) {
        for ((j, pt2) in points.withIndex().take(i)) {
            val d = pt1.zip(pt2) { a, b -> abs(a - b)}.sum()
            if (d <= 3) {
                dsu.merge(i, j)
            }
        }
    }
    val classes = (0 until points.size).map { dsu.getRoot(it) }.toSet()
    println("part 1: ${classes.size}")
}
