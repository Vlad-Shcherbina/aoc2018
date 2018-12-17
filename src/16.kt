enum class Op {
    ADDR, ADDI,
    MULR, MULI,
    BANR, BANI,
    BORR, BORI,
    SETR, SETI,
    GTIR, GTRI, GTRR,
    EQIR, EQRI, EQRR,
}

fun runInsn(op: Op, insn: List<Int>, regs: MutableList<Int>) {
    when (op) {
        Op.ADDR -> regs[insn[3]] = regs[insn[1]] + regs[insn[2]]
        Op.ADDI -> regs[insn[3]] = regs[insn[1]] + insn[2]
        Op.MULR -> regs[insn[3]] = regs[insn[1]] * regs[insn[2]]
        Op.MULI -> regs[insn[3]] = regs[insn[1]] * insn[2]
        Op.BANR -> regs[insn[3]] = regs[insn[1]] and regs[insn[2]]
        Op.BANI -> regs[insn[3]] = regs[insn[1]] and insn[2]
        Op.BORR -> regs[insn[3]] = regs[insn[1]] or regs[insn[2]]
        Op.BORI -> regs[insn[3]] = regs[insn[1]] or insn[2]
        Op.SETR -> regs[insn[3]] = regs[insn[1]]
        Op.SETI -> regs[insn[3]] = insn[1]
        Op.GTIR -> regs[insn[3]] = if (insn[1] > regs[insn[2]]) 1 else 0
        Op.GTRI -> regs[insn[3]] = if (regs[insn[1]] > insn[2]) 1 else 0
        Op.GTRR -> regs[insn[3]] = if (regs[insn[1]] > regs[insn[2]]) 1 else 0
        Op.EQIR -> regs[insn[3]] = if (insn[1] == regs[insn[2]]) 1 else 0
        Op.EQRI -> regs[insn[3]] = if (regs[insn[1]] == insn[2]) 1 else 0
        Op.EQRR -> regs[insn[3]] = if (regs[insn[1]] == regs[insn[2]]) 1 else 0
    }
}

fun main() {
    val lines = java.io.File("data/16.txt").bufferedReader().useLines { it.toList() }
    val it = lines.iterator()

    val opcodes = List(16) { Op.values().toMutableSet() }

    // "Before: [0, 2, 0, 2]"
    val re = """(Before|After):\s+\[(.*)]""".toRegex()

    var cnt = 0
    while (true) {
        var m = re.matchEntire(it.next()) ?: break
        assert(m.groupValues[1] == "Before")
        val valuesBefore = m.groupValues[2].split(", ").map { it.toInt() }
        assert(valuesBefore.size == 4)

        val insn = it.next().split(" ").map { it.toInt() }
        assert(insn.size == 4)

        m = re.matchEntire(it.next()) ?: break
        assert(m.groupValues[1] == "After")
        val valuesAfter = m.groupValues[2].split(", ").map { it.toInt() }
        assert(valuesAfter.size == 4)

        val empty = it.next()
        assert(empty.isEmpty())

        val possibleOps = Op.values().filter { op ->
            val values = valuesBefore.toMutableList()
            runInsn(op, insn, values)
            valuesAfter == values.toList()
        }
        if (possibleOps.size >= 3) {
            cnt++
        }
        opcodes[insn[0]].removeIf { !possibleOps.contains(it) }
    }
    println("part 1: $cnt")
}
