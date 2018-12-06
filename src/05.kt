fun opposite(a: Char, b: Char): Boolean {
    return a.toUpperCase() == b.toUpperCase() && a != b
}

fun main() {
    val (poly) = java.io.File("data/05.txt").inputStream().bufferedReader().useLines { it.toList() }
    val res = mutableListOf<Char>()
    for (c in poly) {
        if (!res.isEmpty() && opposite(c, res[res.size - 1])) {
            res.removeAt(res.size - 1)
        } else {
            res.add(c)
        }
    }
    println("part 1: ${res.size}")
}
