fun main() {
    val lines = java.io.File("data/12.txt").inputStream().bufferedReader().useLines { it.toList() }

    val initialState = lines[0].slice(15 until lines[0].length)
    val re = """(.{5}) => (.)""".toRegex()
    val rules = (lines.slice(2 until lines.size)).map { line ->
        val (left, right) = re.matchEntire(line)!!.destructured
        Pair(left, right[0])
    }.toMap()

    val sums = mutableListOf<Int>()

    var offset = 0
    var state = initialState
    for (step in 0..200) {
        sums.add(state.withIndex().filter { it.value == '#'}.sumBy { it.index + offset} )

        while (!state.startsWith(".....")) {
            state = "." + state
            offset -= 1
        }
        while (!state.endsWith(".....")) {
            state = state + "."
        }
        val newState = mutableListOf<Char>()
        for (i in 0 .. state.length - 5) {
            newState.add(rules[state.substring(i..i + 4)]!!)
        }
        state = newState.joinToString("")
        offset += 2
    }
    println("part 1: ${sums[20]}")
    assert(sums[200] - sums[199] == sums[199] - sums[198])
    println("part 2: ${sums[200] + (sums[200] - sums[199]) * (50000000000 - 200)}")
}
