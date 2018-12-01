fun main() {
    val fin = java.io.File("data/01.txt").inputStream()
    val text = fin.bufferedReader().readText()
    fin.close()
    var s = 0
    for (line in text.trimEnd().lines()) {
        s += line.toInt()
    }
    println(s)
}
