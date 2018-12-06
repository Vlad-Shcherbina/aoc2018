fun opposite(a: Char, b: Char): Boolean {
    return a.toUpperCase() == b.toUpperCase() && a != b
}

fun collapse(xs: String): Int {
    val res = mutableListOf<Char>()
    for (c in xs) {
        if (!res.isEmpty() && opposite(c, res[res.size - 1])) {
            res.removeAt(res.size - 1)
        } else {
            res.add(c)
        }
    }
    return res.size
}

fun main() {
    val (poly) = java.io.File("data/05.txt").inputStream().bufferedReader().useLines { it.toList() }
    println("part 1: ${collapse(poly)}")
    val allTypes = poly.map { it.toUpperCase() }.toSet()
    println("part 2: ${allTypes.map { c -> collapse(poly.filter { it.toUpperCase() != c }) }.min()}")
}
