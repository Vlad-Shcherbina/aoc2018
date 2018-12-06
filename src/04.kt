fun main() {
    val lines = java.io.File("data/04.txt").inputStream().bufferedReader().useLines { it.toMutableList() }

    // Example: "[1518-07-18 23:57] Guard #157 begins shift"
    val re = kotlin.text.Regex("""\[1518-(\d\d)-(\d\d) (\d\d):(\d\d)] (.*)""")
    val guardRe = kotlin.text.Regex("""Guard #(\d+) begins shift""")
    lines.sort()

    val sleepByGuard = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

    var guard: Int? = null
    var fallAsleepTime: Int? = null
    for (line in lines) {
        val (_, _, hourStr, minuteStr, message) = re.matchEntire(line)!!.destructured
        assert(hourStr == "00" || hourStr == "23") { hourStr }
        when (message) {
            "wakes up" -> {
                assert(hourStr == "00") {line}
                val wakeUpTime = minuteStr.toInt()
                sleepByGuard.getOrPut(guard!!) { mutableListOf() }.add(Pair(fallAsleepTime!!, wakeUpTime))
                fallAsleepTime = null
            }
            "falls asleep" -> {
                assert(fallAsleepTime == null)
                assert(hourStr == "00") {line}
                fallAsleepTime = minuteStr.toInt()
            }
            else -> {
                val (g) = guardRe.matchEntire(message)!!.destructured
                guard = g.toInt()
                assert(fallAsleepTime == null)
            }
        }
    }
    val chosenGuard = sleepByGuard.maxBy { kv -> kv.value.sumBy { it.second - it.first } }!!
    val sleepPerMinute = mutableMapOf<Int, Int>()
    for ((begin, end) in chosenGuard.value) {
        for (i in begin until end) {
            sleepPerMinute[i] = (sleepPerMinute[i] ?: 0) + 1
        }
    }
    println("part 1: ${chosenGuard.key * sleepPerMinute.maxBy { it.value}!!.key}")
}
