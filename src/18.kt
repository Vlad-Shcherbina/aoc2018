import java.lang.Error
import kotlin.math.max
import kotlin.math.min

fun next(state: List<List<Char>>): List<List<Char>> {
    val newState = state.map { MutableList(it.size) { '.' } }
    for ((i, row) in state.withIndex()) {
        for ((j, cell) in row.withIndex()) {
            var treeCount = 0
            var lumberyardCount = 0
            for (ii in max(i - 1, 0)..min(i + 1, state.size - 1)) {
                for (jj in max(j - 1, 0)..min(j + 1, state[ii].size - 1)) {
                    if (i == ii && j == jj) {
                        continue
                    }
                    when (state[ii][jj]) {
                        '|' -> treeCount++
                        '#' -> lumberyardCount++
                    }
                }
            }
            newState[i][j] = when (cell) {
                '.' -> if (treeCount >= 3) '|' else '.'
                '|' -> if (lumberyardCount >= 3) '#' else '|'
                '#' -> if (treeCount > 0 && lumberyardCount > 0) '#' else '.'
                else -> throw Error("$cell")
            }
        }
    }
    return newState.map { it.toList() }
}

fun resourceValue(state: List<List<Char>>): Int {
    val t = state.flatten()
    return t.count {it == '|'} * t.count { it == '#' }
}

fun main() {
    val initialState = java.io.File("data/18.txt").bufferedReader().useLines {
        it.map { it.toList() }.toList()
    }
    var s = initialState
    (0..9).forEach { s = next(s) }
    println("part 1: ${ resourceValue(s) }")

    val n = 1_000_000_000
    s = initialState
    var s2 = initialState
    var i = 0
    while (true) {
        s = next(s)
        s2 = next(next(s2))
        i++
        if (i == n || s == s2) {
            break
        }
    }
    (1 .. n % i).forEach { s = next(s) }
    println("part 2: ${ resourceValue(s) }")
}
