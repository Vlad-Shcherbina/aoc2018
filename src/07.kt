fun main() {
    val lines = java.io.File("data/07.txt").inputStream().bufferedReader().useLines { it.toList() }
    val re = kotlin.text.Regex("""Step (.) must be finished before step (.) can begin\.""")
    val steps = mutableSetOf<String>()
    val reqs = mutableMapOf<String, MutableSet<String>>()
    for (line in lines) {
        val (a, b) = re.matchEntire(line)!!.destructured
        steps.add(a)
        steps.add(b)
        reqs.getOrPut(b) { mutableSetOf() }.add(a)
    }
    val seq = mutableListOf<String>()
    while (!steps.isEmpty()) {
        val s = steps.filter { reqs[it].isNullOrEmpty() }.min()!!
        steps.remove(s)
        reqs.values.forEach { it.remove(s) }
        seq.add(s)
    }
    println("part 1: ${seq.joinToString("")}")
}
