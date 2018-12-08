sealed class Task {
    object Child : Task()
    class Metadata(val count: Int): Task()
}

fun main() {
    val line = java.io.File("data/08.txt").inputStream().bufferedReader().useLines { it.single() }
    val xs = line.split(" ").map { it.toInt() }

    var metadataSum = 0

    val i = xs.iterator()
    val tasks = mutableListOf<Task>(Task.Child)
    while (!tasks.isEmpty()) {
        val task = tasks.removeAt(tasks.size - 1)
        when (task) {
            is Task.Child -> {
                val numChildren = i.next()
                val numMetadata = i.next()
                tasks.add(Task.Metadata(numMetadata))
                (1..numChildren).forEach {
                    tasks.add(Task.Child)
                }
            }
            is Task.Metadata -> {
                (1..task.count).forEach { metadataSum += i.next() }
            }
        }
    }
    assert(!i.hasNext())
    println("part 1: $metadataSum")
}
