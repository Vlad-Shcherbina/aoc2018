data class Point(val x: Int, val y: Int, val vx: Int, val vy: Int)

fun main() {
    val lines = java.io.File("data/10.txt").inputStream().bufferedReader().useLines { it.toList() }
    // "position=<-42601, -53357> velocity=< 4,  5>"
    val re = """position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>""".toRegex()
    val points = lines.map { line ->
        println(line)
        val (x, y, vx, vy) = re.matchEntire(line)!!.destructured
        Point(x.toInt(), y.toInt(), vx.toInt(), vy.toInt())
    }
    var t = 0
    while (true) {
        println("t = $t")
        val xs = points.map {it.x + it.vx * t}
        val ys = points.map {it.y + it.vy * t}
        val xRange = xs.min()!!..xs.max()!!
        val yRange = ys.min()!!..ys.max()!!
        if (yRange.count() < 20) {
            for (y in yRange) {
                for (x in xRange) {
                    if (xs.zip(ys).contains(Pair(x, y))) {
                        print("*")
                    } else {
                        print(".")
                    }
                }
                println()
            }
            println("press enter...")
            readLine()
        }
        t++
    }
}
