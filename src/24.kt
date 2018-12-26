import java.io.File
import java.lang.RuntimeException
import kotlin.math.min

data class Group(
    val name: String,
    val side: Int,
    var count: Int,
    val hp: Int,
    val immune: Set<String>, val weak: Set<String>,
    val damage: Int, val attackType: String,
    val initiative: Int) {
    val effectivePower get() = count * damage

    companion object {
        private val re = (
            """(\d+) units each with (\d+) hit points (?:\((.+)\) )?""" +
            """with an attack that does (\d+) (\w+) damage at initiative (\d+)""").toRegex()
        private val re2 = """(weak|immune) to (.+)""".toRegex()
        fun parse(name: String, side: Int, line: String): Group {
            val m = re.matchEntire(line)!!
            val (count, hp, traits, damage, attackType, initiative) = m.destructured
            val weak = mutableSetOf<String>()
            val immune = mutableSetOf<String>()
            if (!traits.isEmpty()) {
                for (trait in traits.split("; ")) {
                    val (t, xs) = re2.matchEntire(trait)!!.destructured
                    when (t) {
                        "weak" -> weak
                        "immune" -> immune
                        else -> throw RuntimeException(t)
                    }.addAll(xs.split(", "))
                }
            }
            return Group(
                name = name,
                side = side,
                count = count.toInt(),
                hp = hp.toInt(),
                immune = immune,
                weak = weak,
                damage = damage.toInt(),
                attackType = attackType,
                initiative = initiative.toInt())
        }
    }
}

fun simulate(groups: MutableList<Group>) {
    while (true) {
//        groups.forEach {
//            println("${it.name} contains ${it.count} units")
//        }
//        println()

        if (groups.map { it.side }.toSet().size < 2) {
            break
        }

        val powerInitiativeCmp = compareBy<Int>(
            { groups[it].effectivePower },
            { groups[it].initiative })

        val targets = mutableSetOf<Int>()
        val attacks = mutableListOf<Pair<Int, Int>>()
        val targetingOrder = (0 until groups.size).sortedWith(powerInitiativeCmp.reversed())
        for (attackerIdx in targetingOrder) {
            val attacker = groups[attackerIdx]
            val targetIdx = (0 until groups.size).filter {
                groups[it].side != attacker.side &&
                        !groups[it].immune.contains(attacker.attackType) &&
                        !targets.contains(it)
            }.maxWith(
                compareBy<Int> { groups[it].weak.contains(attacker.attackType) }
                    .then(powerInitiativeCmp))
            if (targetIdx != null) {
                targets.add(targetIdx)
                attacks.add(Pair(attackerIdx, targetIdx))
            }
        }
        attacks.sortByDescending { groups[it.first].initiative }
        var totalKilled = 0
        for ((attackerIdx, targetIdx) in attacks) {
            val attacker = groups[attackerIdx]
            val target = groups[targetIdx]
            assert(!target.immune.contains(attacker.attackType))
            val damage = attacker.count * attacker.damage *
                    if (target.weak.contains(attacker.attackType)) 2 else 1
            val killed = min(damage / target.hp, target.count)
            target.count -= killed
            totalKilled += killed
//            println("${attacker.name} attacks ${target.name} killing $killed")
        }
//        println()
        groups.removeAll { it.count == 0 }
        if (totalKilled == 0) {
            break
        }
    }
}

fun main() {
    val lines = File("data/24.txt").bufferedReader().useLines { it.toList() }
    val it = lines.iterator()
    val h = it.next()
    assert(h == "Immune System:")
    val immuneSystemGroups = it.asSequence()
        .takeWhile { !it.isEmpty() }
        .withIndex()
        .map { (i, line) -> Group.parse("Immune System group ${i + 1}", 0, line) }.toList()
    val h2 = it.next()
    assert(h2 == "Infection:") { h2 }
    val infectionGroups = it.asSequence()
        .takeWhile { !it.isEmpty() }
        .withIndex().
        map { (i, line) -> Group.parse("Infection group ${i + 1}", 1, line) }.toList()
    assert(!it.hasNext())

    val groups = immuneSystemGroups + infectionGroups

    val g1 = groups.map { it.copy() }.toMutableList()
    simulate(g1)
    println("part 1: ${g1.sumBy { it.count }}")

    var boost = 0
    while (true) {
        val g = groups.map {
            it.copy(damage = it.damage + if (it.side == 0) boost else 0)
        }.toMutableList()
        simulate(g)
        if (g.map { it.side }.toSet() == setOf(0)) {
            println("part 2: ${g.sumBy { it.count }}")
            break
        }
        boost++
    }
}
