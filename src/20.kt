sealed class Path {
    data class Step(val dir: Char): Path()
    data class Seq(val children: List<Path>): Path()
    data class Or(val children: List<Path>): Path()
}

class Parser(val line: String, var pos: Int) {
    fun parseSeq(): Path {
        val children = mutableListOf<Path>()
        while (true) {
            if (line[pos] == '(') {
                children.add(parseGroup())
            } else if (line[pos].isUpperCase()){
                children.add(Path.Step(line[pos]))
                pos++
            } else {
                break
            }
        }
        return Path.Seq(children)
    }
    fun parseGroup(): Path {
        assert(line[pos] == '(')
        pos++
        val children = mutableListOf<Path>()
        while (true) {
            children.add(parseSeq())
            if (line[pos] == ')') {
                pos++
                break
            } else if (line[pos] == '|'){
                pos++
            } else {
                assert(false) { line[pos] }
            }
        }
        return Path.Or(children)
    }
}

data class Pt(val x: Int, val y: Int)

fun enumPaths(path: Path, starts: Set<Pt>, edges: MutableSet<Pair<Pt, Pt>>): Set<Pt> {
    when (path) {
        is Path.Step -> {
            var dx = 0
            var dy = 0
            when (path.dir) {
                'W' -> dx = -1
                'N' -> dy = -1
                'E' -> dx = 1
                'S' -> dy = 1
                else -> assert(false) { path }
            }
            return starts.map {
                val dst = Pt(it.x + dx, it.y + dy)
                edges.add(Pair(it, dst))
                edges.add(Pair(dst, it))
                dst
            }.toSet()
        }
        is Path.Seq -> {
            var pts = starts
            path.children.forEach { pts = enumPaths(it, pts, edges) }
            return pts
        }
        is Path.Or -> {
            return path.children.flatMap { enumPaths(it, starts, edges) }.toSet()
        }
    }
}

fun computeDists(edges: Set<Pair<Pt, Pt>>): Map<Pt, Int> {
    val adj = edges.groupBy { it.first }.mapValues { it.value.map { it.second }}
    val dists = mutableMapOf<Pt, Int>()
    var q = listOf(Pt(0, 0))
    dists[Pt(0, 0)] = 0
    var d = 0
    while (!q.isEmpty()) {
        d++
        val newQ = mutableListOf<Pt>()
        for (x in q) {
            for (y in adj[x]!!) {
                if (!dists.contains(y)) {
                    dists[y] = d
                    newQ.add(y)
                }
            }
        }
        q = newQ
    }
    return dists
}

fun main() {
    val line = java.io.File("data/20.txt").bufferedReader().useLines { it.single() }
    val parser = Parser(line, 1)
    val p = parser.parseSeq()
    assert(parser.pos == line.length - 1)

    val edges = mutableSetOf<Pair<Pt, Pt>>()
    enumPaths(p, setOf(Pt(0, 0)), edges)

    val dists = computeDists(edges)
    println("part 1: ${dists.values.max()}")
    println("part 1: ${dists.values.count { it >= 1000 }}")
}
