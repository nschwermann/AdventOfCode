/**
 * https://adventofcode.com/2020/day/8
 * --- Handheld Halting ---
 */
fun main() {

    val input = ResourceReader(8).text().let { CodeParser().parseCode(it) }
    println(part1(input))
    println(part2(input))
}

private fun part1(program : List<BootCode>) : Int{
    return HandHeldVM().runWithHalt(program)
}

private fun part2(program: List<BootCode>) : Int{
    return HandHeldVM().runRepair(program)
}

class CodeParser {
    private val pattern = "(\\w+) (\\+|-)(\\d+)$".toRegex(RegexOption.MULTILINE)

    fun parseCode(text : String) : List<BootCode>{
        return pattern.findAll(text).map {
            val (code, sign, value) = it.destructured
            val arg = when(sign){
                "-" -> -value.toInt()
                else -> value.toInt()
            }
            when(code){
                "acc" -> Acc(arg)
                "jmp" -> Jmp(arg)
                else -> NoOp(arg)
            }
        }.toList()
    }
}

class HandHeldVM{
    private var index = 0
    private var _accumulator = 0

    fun runWithHalt(program : List<BootCode>) : Int {
        val debugger = BooleanArray(program.size)
        while(!debugger[index]){
            debugger[index] = true
            runInstruction(program[index])
        }
        return _accumulator
    }

    fun runRepair(program: List<BootCode>) : Int {
        val debugger = BooleanArray(program.size)
        val indexSeq  = sequence {
            var last = program.size - 1
            while(true){
                if(program[last] is NoOp || program[last] is Jmp) yield(last)
                last--
            }
        }.iterator()

        while(true){
            val nextIndexRepair : Int = indexSeq.next()
            val repairedInstruction = program[nextIndexRepair].let {
                when(it){
                    is NoOp -> Jmp(it.arg)
                    else -> NoOp(it.arg)
                }
            }
            val repairedCode = program.let {
                it.toMutableList().apply {
                    set(nextIndexRepair, repairedInstruction)
                }
            }
            index = 0
            _accumulator = 0
            for(i in debugger.indices){debugger[i] = false}
            while(true){
                if(index == program.size) return _accumulator.also {
                    println("Repaired Instruction at index $nextIndexRepair ${program[nextIndexRepair]} → $repairedInstruction")
                }
                if(debugger[index]) break
                debugger[index] = true
                runInstruction(repairedCode[index])
            }
        }
    }

    private fun runInstruction(bootCode: BootCode) = when(bootCode){
        is Acc -> {
            _accumulator += bootCode.arg
            index += 1
        }
        is NoOp -> index += 1
        is Jmp -> index += bootCode.arg
    }
}
sealed class BootCode(open val arg : Int)
data class Acc(override val arg: Int) : BootCode(arg)
data class NoOp(override val arg : Int) : BootCode(arg)
data class Jmp(override val arg : Int) : BootCode(arg)