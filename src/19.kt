data class Insn(val op: Op, val insn: List<Int>)

fun main() {
    val lines = java.io.File("data/19.txt").bufferedReader().useLines { it.toList() }

    val program =
    lines.slice(1 until lines.size).map { line ->
        val insnStr = line.split(" ")
        val insn = Insn(
            Op.values().single { it.toString().toLowerCase() == insnStr[0] },
            insnStr.map { it.toIntOrNull() ?: -1 })
        insn
    }
    val ipIdx = 1

    for (start in 0..1) {
        val regs = MutableList(6) { 0 }
        regs[0] = start
        while (regs[ipIdx] >= 0 && regs[ipIdx] < program.size) {
            if (regs[1] == 3) {
                var tick = 0
                while (regs[3] * regs[5] != regs[2] && regs[5] + 1 < regs[2]) {
                    tick++
                    if (regs[2] % regs[3] != 0) {
                        regs[5] = regs[2] - 1
                    } else
                        regs[5]++
                }
            }

            val insn = program[regs[ipIdx]]
            runInsn(insn.op, insn.insn, regs)
            regs[ipIdx]++
        }
        println("part ${start + 1}: ${regs[0]}")
    }
}
