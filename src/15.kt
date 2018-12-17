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

fun distsFrom(row: Int, col: Int, board: Board): List<List<Int?>> {
    val dists = board.map { MutableList<Int?>(it.size) { null } }
    var q = mutableListOf(Pair(row, col))
    dists[row][col] = 0
    var d = 0
    while (!q.isEmpty()) {
        d++
        val newQ = mutableListOf<Pair<Int, Int>>()
        for ((r, c) in q) {
            assert(dists[r][c] != null)
            for ((r2, c2) in neighbors(r, c)) {
                if (board[r2][c2] == '.' && dists[r2][c2] == null) {
                    dists[r2][c2] = d
                    newQ.add(Pair(r2, c2))
                }
            }
        }
        q = newQ
    }
    return dists
}

fun bestMoveDestination(unit: Unit, units: List<Unit>, board: Board): Pair<Int, Int>? {
    val dists = distsFrom(unit.row, unit.col, board)
    val dst = units.filter { it.hp > 0 && it.force != unit.force }
        .flatMap { neighbors(it.row, it.col) }
        .filter { dists[it.first][it.second] != null}
        .minWith(compareBy({ dists[it.first][it.second] }, { it.first }, {it.second }))
        ?: return null
    val dists2 = distsFrom(dst.first, dst.second, board)
    return neighbors(unit.row, unit.col)
        .filter { dists2[it.first][it.second] != null }
        .minWith(compareBy({ dists2[it.first][it.second] }, { it.first }, { it.second }))!!
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

fun runBattle(board: Board, attackByForce: Map<Char, Int>): Pair<Int, List<Unit>> {
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
                val dst = bestMoveDestination(unit, units, board) ?: continue
                move(unit, dst, board)
                target = bestTargetInRange(unit, units)
            }
            if (target != null) {
                target.hp -= attackByForce[unit.force]!!
                if (target.hp <= 0) {
                    board[target.row][target.col] = '.'
                }
            }
        }
        if (done) {
            return Pair(round, units.filter { it.hp > 0 })
        }
        round++
    }
}

fun main() {
    val board: Board = java.io.File("data/15.txt").inputStream().bufferedReader().useLines {
        it.map { it.toCharArray().toMutableList() }.toList()
    };

    {
        val (round, units) = runBattle(
            board.map { it.toMutableList() },
            mapOf('E' to 3, 'G' to 3)
        )
        println("part 1: ${round * units.sumBy { it.hp }}")
    }()

    val initialElves = board.sumBy { row -> row.count {it == 'E'} }
    var attack = 3
    while (true) {
        attack++
        val (round, units) = runBattle(board.map {
            it.toMutableList() },
            mapOf('E' to attack, 'G' to 3))
        if (units.size == initialElves && units.all {it.force == 'E' }) {
            println("part 2: ${round * units.sumBy { it.hp }}")
            break
        }
    }
}
