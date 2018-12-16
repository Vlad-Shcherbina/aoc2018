import kotlin.math.abs

data class Unit(val force: Char, var row: Int, var col: Int, var hp: Int)

typealias Board = List<MutableList<Char>>

fun dist(u1: Unit, u2: Unit): Int {
    return abs(u1.row - u2.row) + abs(u1.col - u2.col)
}

fun bestTargetInRange(unit: Unit, units: List<Unit>): Unit? {
    return units.filter { it.hp > 0 && it.force != unit.force && dist(it, unit) == 1 }
        .minWith(compareBy({ it.hp}, {it.row}, {it.col}))
}

fun neighbors(row: Int, col: Int): List<Pair<Int, Int>> =
    listOf(
        Pair(row - 1, col),
        Pair(row, col - 1),
        Pair(row, col + 1),
        Pair(row + 1, col))

fun bestMoveDestination(unit: Unit, units: List<Unit>, board: Board): Pair<Int, Int>? {
    val dists = board.map { MutableList(it.size) { -1 } }
    var q = mutableListOf<Pair<Int, Int>>()
    for (u in units) {
        if (u.hp <= 0 || u.force == unit.force) {
            continue
        }
        assert(dists[u.row][u.col] == -1)
        dists[u.row][u.col] = 0
        q.add(Pair(u.row, u.col))
    }
    var d = 1
    while (!q.isEmpty()) {
        val newQ = mutableListOf<Pair<Int, Int>>()
        for (x in q) {
            for ((r, c) in neighbors(x.first, x.second)) {
                if (board[r][c] == '.' && dists[r][c] == -1) {
                    dists[r][c] = d
                    newQ.add(Pair(r, c))
                }
            }
        }
        q = newQ
        d += 1
    }
    return neighbors(unit.row, unit.col)
        .filter { dists[it.first][it.second] > 0 }
        .minWith(compareBy({ dists[it.first][it.second] }, {it.first}, {it.second} ))
}

fun move(unit: Unit, dst: Pair<Int, Int>, board: Board) {
    val (newRow, newCol) = dst
    assert(board[unit.row][unit.col] == unit.force)
    board[unit.row][unit.col] = '.'
    unit.row = newRow
    unit.col = newCol
    assert(board[unit.row][unit.col] == '.')
    board[unit.row][unit.col] = unit.force
}

fun main() {
    val board: Board = java.io.File("data/15.txt").inputStream().bufferedReader().useLines {
        it.map { it.toCharArray().toMutableList() }.toList()
    }
    val units = board.withIndex().flatMap { (i, row) ->
        row.withIndex().flatMap { (j, ch) ->
            if (ch == 'E' || ch == 'G') {
                listOf(Unit(force = ch, row = i, col = j, hp = 200))
            } else {
                emptyList()
            }
        }
    }.toMutableList()

    var round = 0
    while (true) {
        units.sortWith(compareBy({ it.row }, { it.col }))
        println("round $round")
        units.forEach { if (it.hp > 0) println(it) }
        for (row in board) {
            println(row.joinToString(" "))
        }
        var done = false
        for (unit in units) {
            if (unit.hp <= 0) {
                continue
            }
            if (units.none { it.hp > 0 && it.force != unit.force }) {
                done = true
                break
            }
            var target = bestTargetInRange(unit, units)
            if (target == null) {
                // move
                val dst = bestMoveDestination(unit, units, board) ?: continue
                move(unit, dst, board)
                target = bestTargetInRange(unit, units)
            }
            if (target != null) {
                // attack
                target.hp -= 3
                if (target.hp <= 0) {
                    board[target.row][target.col] = '.'
                }
            }
        }
        if (done) {
            val hpSum = units.filter { it.hp > 0 }.sumBy { it.hp }
            println("part 1: ${round * hpSum}")
            break
        }
        round++
    }
}
