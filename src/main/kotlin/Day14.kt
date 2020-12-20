/**
 * --- Day 14: Docking Data ---
 * https://adventofcode.com/2020/day/14
 */

private fun main() {
    val maskPattern = "mask = (\\w+)".toRegex()
    val writePattern = "mem\\[(\\d+)\\] = (\\d+)".toRegex()

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

private fun part1(input : List<InstructionSet>) : Long {
    val mem = input.fold(mutableMapOf<Long, Long>()){acc, next ->
        acc.apply {
            next.commands.forEach {
                put(it.first, it.second or next.mOn and next.mOff)
            }
        }
    }
    return mem.values.sum()
}

private fun part2(input : List<InstructionSet>) : Long{
    val mem = input.fold(mutableMapOf<Long, Long>()){acc, next ->
        acc.apply {
            val diff = next.mOff xor next.mOn

            next.commands.forEach {(addr, value) ->
                for(i in 0 until (1 shl diff.countOneBits())){
                    var x =  addr or next.mOff
                    var k = diff
                    for(j in 0 until diff.countOneBits()){
                        x = k xor k - (i shr j and 1) and diff xor x
                        k = k and k - 1
                    }
                    acc[x] = value
                }
            }
        }
    }
    return mem.values.sum()
}

private class InstructionSet(mask : String, val commands : List<Pair<Long, Long>>){
    val mOff = mask.replace('X', '1').toLong(2)
    val mOn = mask.replace('X', '0').toLong(2)
}