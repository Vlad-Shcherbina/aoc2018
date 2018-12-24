import kotlin.math.abs

data class Bot(val x: Int, val y: Int, val z: Int, val r: Int)

data class Subspace(
    val xMin: Int, val yMin: Int, val zMin: Int,
    val xMax: Int, val yMax: Int, val zMax: Int) {
    fun dist(x: Int, y: Int, z: Int): Int {
        return distToSegment(xMin, xMax, x) +
               distToSegment(yMin, yMax, y) +
               distToSegment(zMin, zMax, z)
    }

    fun isPoint(): Boolean {
        return xMin == xMax && yMin == yMax && zMin == zMax
    }

    fun split(): List<Subspace> {
        assert(!isPoint())
        val xSegs = splitSegment(xMin, xMax)
        val ySegs = splitSegment(yMin, yMax)
        val zSegs = splitSegment(zMin, zMax)
        return xSegs.flatMap { xs ->
            ySegs.flatMap {ys ->
                zSegs.map {zs ->
                    Subspace(xs.first, ys.first, zs.first,
                             xs.second, ys.second, zs.second)
                }
            }
        }
    }
}

fun distToSegment(xMin: Int, xMax: Int, x: Int): Int {
    return when {
        x < xMin -> xMin - x
        x > xMax -> x - xMax
        else -> 0
    }
}

fun splitSegment(xMin: Int, xMax: Int): List<Pair<Int, Int>> {
    assert(xMin <= xMax)
    return if (xMin == xMax) {
        listOf(Pair(xMin, xMax))
    } else {
        val m = (xMin + xMax) / 2
        listOf(Pair(xMin, m), Pair(m + 1, xMax))
    }
}

fun main() {
    // "pos=<31677325,-4802168,27906742>, r=53836958"
    val re = """pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(-?\d+)""".toRegex()
    val bots = java.io.File("data/23.txt").bufferedReader().useLines { lines ->
        lines.map { line ->
            val (x, y, z, r) = re.matchEntire(line)!!.destructured
            Bot(x.toInt(), y.toInt(), z.toInt(), r.toInt())
        }.toList()
    }
    val strongestBot = bots.maxBy { it.r }!!
    val part1 = bots.count {
        abs(it.x - strongestBot.x) +
        abs(it.y - strongestBot.y) +
        abs(it.z - strongestBot.z) <= strongestBot.r }
    println("part 1: $part1")

    val root = Subspace(
        xMin = bots.map { it.x }.min()!!,
        yMin = bots.map { it.y }.min()!!,
        zMin = bots.map { it.z }.min()!!,
        xMax = bots.map { it.x }.max()!!,
        yMax = bots.map { it.y }.max()!!,
        zMax = bots.map { it.z }.max()!!)

    val q = java.util.PriorityQueue<Pair<Subspace, List<Bot>>>(
        compareBy<Pair<Subspace, List<Bot>>> { it.second.size }.reversed()
            .thenBy { it.first.dist(0, 0, 0)} )
    q.add(Pair(root, bots))

    while (true) {
        val s = q.poll()
        if (s.first.isPoint()) {
            println("part 2: ${s.first.dist(0, 0, 0)}")
            break
        }
        for (s2 in s.first.split()) {
            q.add(Pair(s2, s.second.filter { s2.dist(it.x, it.y, it.z) <= it.r } ))
        }
    }
}
