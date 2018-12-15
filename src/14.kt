const val INPUT = 637061

fun main() {
    val recipes = mutableListOf(3, 7)
    var first = 0
    var second = 1
    val exp = INPUT.toString().map { it.toInt() - '0'.toInt() }
    while (true) {
        if (recipes.size == INPUT + 10) {
            print("part 1: ")
            for (c in recipes.slice(INPUT until INPUT + 10))
                print(c)
            println()
        }
        val c = recipes[first] + recipes[second]
        if (c < 10) {
            recipes.add(c)
        } else {
            recipes.add(c / 10)
            if (recipes.size >= exp.size &&
                recipes.slice(recipes.size - exp.size until recipes.size) == exp) {
                println("part 2: ${recipes.size - exp.size}")
                break
            }
            recipes.add(c % 10)
        }
        if (recipes.size >= exp.size &&
            recipes.slice(recipes.size - exp.size until recipes.size) == exp) {
            println("part 2: ${recipes.size - exp.size}")
            break
        }
        first += recipes[first] + 1
        first %= recipes.size
        second += recipes[second] + 1
        second %= recipes.size
    }
}
