/**
 * --- Day 14: Docking Data ---
 * https://adventofcode.com/2020/day/14
 */

fun main() {
    val maskPattern = "mask = (\\w+)".toRegex()
    val writePattern = "mem\\[(\\d+)\\] = (\\d+)".toRegex()

    val test = """
        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        mem[8] = 11
        mem[7] = 101
        mem[8] = 0
    """.trimIndent()
    //ResourceReader(14).text()
    val input = ResourceReader(14).text().let {
        val instructions = mutableListOf<InstructionSet>()
        var curMask : String? = null
        val curInstructions = mutableListOf<Pair<Long, Long>>()
        for(line in it.lines()) {
            if (line.matches(maskPattern)) {
                curMask?.let {
                    instructions += InstructionSet(it, curInstructions.toList())
                }?.also {
                    curMask = null
                    curInstructions.clear()
                }
                curMask = maskPattern.find(line)!!.destructured.component1()
            } else {
                writePattern.find(line)!!.destructured.let { (first,sec) -> curInstructions.add(first.toLong() to sec.toLong()) }
            }
        }
        instructions += InstructionSet(curMask!!, curInstructions.toList())
        instructions.toList()
    }

    println(part1(input))
    println(part2(input))
}

//2346881602152
private fun part1(input : List<InstructionSet>) : Long {
    val mem = input.fold(mutableMapOf<Long, Long>()){acc, next ->
        val onMask = next.mask.replace('X', '0').toLong(2)
        val offMask = next.mask.replace('X', '1').toLong(2)
        acc.apply {
            next.commands.forEach {
                put(it.first, it.second or onMask and offMask)
            }
        }
    }
    return mem.values.sum()
}

private fun part2(input : List<InstructionSet>) : Long{
    val dupRemoved = input.fold(mutableListOf<InstructionSet>()){acc, next ->
        if(!acc.contains(next)) acc+= next
        acc
    }
    println("hasDuplicates: ${dupRemoved != input}")
    return 1L
}

private data class InstructionSet(val mask : String, val commands : List<Pair<Long, Long>>)