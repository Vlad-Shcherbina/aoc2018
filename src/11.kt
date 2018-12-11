const val gridSize = 300
const val gridSerialNumber = 5535

fun main() {
    val acc = (0..gridSize).map { (0..gridSize).map { 0 }.toMutableList() }
    for (x in 1..gridSize) {
        for (y in 1..gridSize) {
            val rackId = x + 10
            acc[y][x] = (rackId * y + gridSerialNumber) * rackId % 1000 / 100 - 5
        }
    }
    for (x in 1..gridSize) {
        for (y in 0..gridSize) {
            acc[y][x] += acc[y][x - 1]
        }
    }
    for (x in 0..gridSize) {
        for (y in 1..gridSize) {
            acc[y][x] += acc[y - 1][x]
        }
    }
    val solve = { sizeRange: IntRange ->
        var bestX = -1
        var bestY = -1
        var bestSize = -1
        var best = -1000000
        for (size in sizeRange) {
            for (x in 0..gridSize - size) {
                for (y in 0..gridSize - size) {
                    val s = acc[y + size][x + size] + acc[y][x]
                          - acc[y][x + size] - acc[y + size][x]
                    if (s > best) {
                        best = s
                        bestX = x + 1
                        bestY = y + 1
                        bestSize = size
                    }
                }
            }
        }
        "$bestX,$bestY,$bestSize"
    }
    println("part 1: ${solve(3..3)}")
    println("part 2: ${solve(1..gridSize)}")
}
