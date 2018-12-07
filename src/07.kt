fun simulate(orig_reqs: Map<Char, Set<Char>>, numWorkers: Int): Pair<String, Int> {
    val reqs = orig_reqs.toMutableMap().mapValues { it.value.toMutableList() }
    val steps = reqs.keys.toMutableSet()
    reqs.values.forEach { steps.addAll(it) }
    val seq = mutableListOf<Char>()
    val workers = mutableListOf<Pair<Char, Int>>()
    var t = 0
    while (!steps.isEmpty() || !workers.isEmpty()) {
        while (!steps.isEmpty() && workers.size < numWorkers) {
            val step = steps.filter { reqs[it].isNullOrEmpty() }.min() ?: break
            val stepTime = step - 'A' + 60 + 1
            workers.add(Pair(step, t + stepTime))
            steps.remove(step)
            seq.add(step)
        }
        if (!workers.isEmpty()) {
            val w = workers.minBy { it.second }!!
            workers.remove(w)
            reqs.values.forEach { it.remove(w.first) }
            t = w.second
        }
    }
    return Pair(seq.joinToString(""), t)
}

fun main() {
    val lines = java.io.File("data/07.txt").inputStream().bufferedReader().useLines { it.toList() }
    val re = kotlin.text.Regex("""Step (.) must be finished before step (.) can begin\.""")
    val reqs = mutableMapOf<Char, MutableSet<Char>>()
    for (line in lines) {
        val (a, b) = re.matchEntire(line)!!.destructured
        assert(a.length == 1)
        assert(b.length == 1)
        reqs.getOrPut(b[0]) { mutableSetOf() }.add(a[0])
    }
    println("part 1: ${simulate(reqs, 1).first}")
    println("part 2: ${simulate(reqs, 5).second}")
}
