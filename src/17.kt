data class Box(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun main() {
    // "y=7, x=495..501"
    val horRe = """y=(\d+), x=(\d+)\.\.(\d+)""".toRegex()
    val verRe = """x=(\d+), y=(\d+)\.\.(\d+)""".toRegex()
    val boxes = java.io.File("data/17.txt").bufferedReader().useLines { lines ->
        lines.map {
            var m = horRe.matchEntire(it)
            if (m != null) {
                val (y, x1, x2) = m.destructured
                Box(x1.toInt(), y.toInt(), x2.toInt(), y.toInt())
            } else {
                m = verRe.matchEntire(it)
                val (x, y1, y2) = m!!.destructured
                Box(x.toInt(), y1.toInt(), x.toInt(), y2.toInt())
            }
        }.toList()
    }

    val xMin = boxes.map { it.x1 }.min()!!
    val xMax = boxes.map { it.x2 }.max()!!
    val yMin = boxes.map { it.y1 }.min()!!
    val yMax = boxes.map { it.y2 }.max()!!

    val grid = (yMin..yMax).map{ (xMin - 1 .. xMax + 1).map { '.' }.toMutableList() }
    boxes.forEach { box ->
        (box.y1..box.y2).forEach { y ->
            (box.x1..box.x2).forEach { x ->
                grid[y - yMin][x - xMin + 1] = '#'
            }
        }
    }

    val q = java.util.PriorityQueue<Pair<Int, Int>>(compareBy({ -it.first }, { it.second }))

    fun horTrace(y: Int, x0: Int, dx: Int): Pair<Boolean, Int> {
        var x = x0
        while (true) {
            if (grid[y + 1][x] != '#' && grid[y + 1][x] != '~') {
                return Pair(true, x)
            }
            if (grid[y][x + dx] == '#') {
                return Pair(false, x)
            }
            x += dx
        }
    }

    assert(grid[0][500 - xMin + 1] == '.')
    grid[0][500 - xMin + 1] = '|'
    q.add(Pair(0, 500 - xMin + 1))

    while (!q.isEmpty()) {
        val (y, x) = q.poll()
        if (grid[y][x] == '~') {
            continue
        }
        assert(grid[y][x] == '|')
        if (y + 1 == grid.size) {
            continue
        }
        if (grid[y + 1][x] == '.') {
            q.add(Pair(y, x))
            grid[y + 1][x] = '|'
            q.add(Pair(y + 1, x))
            continue
        }
        if (grid[y + 1][x] == '|') {
            continue
        }
        val (openLeft, xLeft) = horTrace(y, x, -1)
        val (openRight, xRight) = horTrace(y, x, 1)
        if (!openLeft && !openRight) {
            (xLeft..xRight).forEach { grid[y][it] = '~' }
            continue
        }
        var alive = false
        (xLeft..xRight).forEach { grid[y][it] = '|' }
        if (openLeft && grid[y + 1][xLeft] == '.') {
            grid[y + 1][xLeft] = '|'
            q.add(Pair(y + 1, xLeft))
            alive = true
        }
        if (openRight && grid[y + 1][xRight] == '.') {
            grid[y + 1][xRight] = '|'
            q.add(Pair(y + 1, xRight))
            alive = true
        }
        if (alive) {
            q.add(Pair(y, x))
        }
    }

    println("part 1: ${grid.flatten().count { it == '|' || it == '~' }}")
    println("part 1: ${grid.flatten().count { it == '~' }}")
}
