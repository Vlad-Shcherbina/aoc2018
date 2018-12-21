fun main() {
    val lines = java.io.File("data/21.txt").bufferedReader().useLines { it.toList() }
    val program =
        lines.slice(1 until lines.size).map { line ->
            val insnStr = line.split(" ")
            val insn = Insn(
                Op.values().single { it.toString().toLowerCase() == insnStr[0] },
                insnStr.map { it.toIntOrNull() ?: -1 })
            insn
        }
    val ipIdx = 4

    val occur = mutableSetOf<Int>()
    var first = true
    var last = -1

    val regs = MutableList(6) { 0 }
    regs[0] = -1
    var cnt = 0
    while (regs[ipIdx] >= 0 && regs[ipIdx] < program.size) {
        cnt++
        if (cnt % 100000000 == 0) {
            println("working...")
        }
        val insn = program[regs[ipIdx]]
        if (regs[ipIdx] == 28) {
            val c = regs[2]
            if (first) {
                println("part 1: $c")
                first = false
            }
            if (!occur.contains(c)) {
                occur.add(c)
                last = c
            } else {
                println("part 2: $last")
                break
            }
        }
        runInsn(insn.op, insn.insn, regs)
        regs[ipIdx]++
    }
}
