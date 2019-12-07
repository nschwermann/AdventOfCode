package _2019

import ResourceReader

fun main() {
    val input = ResourceReader(2019, 5).intArray()

    sequence(input).fold(1 as Int?){code , instruction ->
        instruction.execute(input, code)
    }.let { println(it) }

}

fun sequence(array : IntArray) = sequence {
    val iterator = array.iterator()
    while(true){
        iterator.nextInt().run {
            toString().padStart(5, '0').let {
                Program.forOpCode(it.takeLast(2).toInt()) to
                        it.take(3).reversed().map(Character::getNumericValue).iterator()
            }
        }.let {(program, modes) ->
            if(program is Program.Term) return@sequence

            val args = Array(program.params){
                Argument(Mode.values()[modes.next()], iterator.nextInt())
            }
            yield(Instructions(program, args))
        }
    }
}

enum class Mode{
    Position,
    Immediate
}

data class Argument(val mode : Mode, val value : Int){
    fun eval(mem : IntArray) : Int {
        return when(mode) {
            Mode.Position -> mem[value]
            Mode.Immediate -> value
        }
    }
}

sealed class Program(private val opCode : Int, val params: Int){
    object Term : Program(99, 0)
    object Add : Program(1, 3)
    object Mul : Program(2, 3)
    object Store : Program(3, 1)
    object Out : Program(4, 1)

    companion object{
        fun forOpCode(code : Int) : Program{
            return Program::class.nestedClasses.flatMap {
                if(it.isCompanion) emptyList<Program>()
                else listOf(it.objectInstance as Program)
            }.first {
                code == it.opCode
            }
        }
    }
}

class Instructions(private val program: Program, private val args : Array<Argument>){
    fun execute(memory : IntArray, input : Int?) : Int? {
        return when(program){
            is Program.Add -> {
                memory[args[2].value] = args[0].eval(memory) + args[1].eval(memory)
                null
            }
            is Program.Mul -> {
                memory[args[2].value] = args[0].eval(memory) * args[1].eval(memory)
                null
            }
            is Program.Store -> {
                memory[args[0].value] = input!!
                null
            }
            is Program.Out -> args[0].eval(memory)
            is Program.Term -> null
        }
    }
}