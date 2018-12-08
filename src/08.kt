sealed class Task {
    object Child : Task()
    class Metadata(val numChildren: Int, val count: Int): Task()
}

fun main() {
    val line = java.io.File("data/08.txt").inputStream().bufferedReader().useLines { it.single() }
    val xs = line.split(" ").map { it.toInt() }

    var metadataSum = 0

    val i = xs.iterator()
    val tasks = mutableListOf<Task>(Task.Child)
    val nodeValues = mutableListOf<Int>()
    while (!tasks.isEmpty()) {
        val task = tasks.removeAt(tasks.size - 1)
        when (task) {
            is Task.Child -> {
                val numChildren = i.next()
                val numMetadata = i.next()
                tasks.add(Task.Metadata(numChildren, numMetadata))
                (1..numChildren).forEach { tasks.add(Task.Child) }
            }
            is Task.Metadata -> {
                val children = nodeValues.slice(nodeValues.size - task.numChildren until nodeValues.size)
                (1..task.numChildren).forEach { nodeValues.removeAt(nodeValues.size - 1) }
                var nodeValue = 0
                (1..task.count).forEach {
                    val m = i.next()
                    metadataSum += m
                    if (children.isEmpty()) {
                        nodeValue += m
                    } else {
                        if (m >= 1 && m <= children.size) {
                            nodeValue += children[m - 1]
                        }
                    }
                }
                nodeValues.add(nodeValue)
            }
        }
    }
    assert(!i.hasNext())
    println("part 1: $metadataSum")
    println("part 2: ${nodeValues.single()}")
}
