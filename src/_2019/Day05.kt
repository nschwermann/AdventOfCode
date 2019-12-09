package _2019

import ResourceReader
import java.lang.IllegalStateException

fun main() {
    val input = ResourceReader(2019, 5).intArray()

    println("Part1 : ${sequence(input.copyOf(), 1).last().value}")
    println("Part2 : ${sequence(input.copyOf(), 5).last().value}")
}

fun sequence(array: IntArray, input : Int) = generateSequence(PV(Pointer(0), Value.Something(input))){ output ->
    array.execute(output)
}

fun OpAndModes.getArguments(memory: IntArray, pointer: Pointer) = (0 until op.params).map{ index->
    Argument(memory[pointer.value + 1 + index], modes[index])
}

fun IntArray.getOperation(pointer: Pointer) : OpAndModes = get(pointer.value).toOpMode()


data class OpAndModes(val op : Operation, val modes : List<Argument.Mode>)
fun Int.toOpMode() : OpAndModes{
    return toString().padStart(5, '0').let { word ->
        OpAndModes(
            Operation.forOpCode(word.takeLast(2).toInt()),
            word.take(3).reversed().map(Character::getNumericValue).map { Argument.Mode.values()[it] }
        )
    }
}

inline class Pointer(val value : Int)
operator fun Pointer.plus(op : Operation) : Pointer{
    return Pointer(value + 1 + op.params)
}
operator fun Pointer.plus(value : Int) : Int {
    return this.value + value 
}

sealed class Value{
    data class Something(val value: Int) : Value()
    object None : Value()
    fun eval() : Int {
        return when(this) {
            is Something -> value
            else -> throw IllegalStateException("Value was None")
        }
    }
}

data class PV(val pointer : Pointer, val value : Value)

data class Argument (private val value : Int, private val mode : Mode){

    enum class Mode{
        Position,
        Immediate
    }

    fun write(memory: IntArray, write: Int){
        memory[value] = write
    }

    fun eval(memory: IntArray) : Int {
        return when(mode) {
            Mode.Position -> memory[value]
            Mode.Immediate -> value
        }
    }
}

sealed class Operation(private val code : Int, val params: Int){
    object Term : Operation(99, 0)
    object Add : Operation(1, 3)
    object Mul : Operation(2, 3)
    object Store : Operation(3, 1)
    object Out : Operation(4, 1)
    object JIT : Operation(5, 2)
    object JIF : Operation(6, 2)
    object LessThan : Operation(7, 3)
    object Eq : Operation(8, 3)

    companion object{
        fun forOpCode(code : Int) : Operation{
            return Operation::class.nestedClasses.flatMap {
                if(it.isCompanion) emptyList<Operation>()
                else listOf(it.objectInstance as Operation)
            }.first {
                code == it.code
            }
        }
    }
}

fun IntArray.execute(input: PV) : PV? {
    val pointer = input.pointer
    val value = input.value
    val opAndModes = getOperation(pointer)

    return if(opAndModes.op == Operation.Term) return null
    else {
        val args = opAndModes.getArguments(this, pointer)
        when(opAndModes.op){
            is Operation.Add -> {
                args[2].write(this, args[0].eval(this) + args[1].eval(this))
                PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.Mul -> {
                args[2].write(this,args[0].eval(this) * args[1].eval(this))
                PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.Store -> {
                args[0].write(this, value.eval())
                PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.Out -> {
                PV(pointer + opAndModes.op, Value.Something(args[0].eval(this)))
            }
            is Operation.JIT -> {
                if(args[0].eval(this)  != 0) PV(Pointer(args[1].eval(this)), input.value)
                else PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.JIF -> {
                if(args[0].eval(this) == 0) PV(Pointer(args[1].eval(this)), input.value)
                else PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.LessThan -> {
                if(args[0].eval(this) < args[1].eval(this)){
                    args[2].write(this, 1)
                } else {
                    args[2].write(this, 0)
                }
                PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.Eq ->{
                if(args[0].eval(this) == args[1].eval(this)) {
                    args[2].write(this, 1)
                }else {
                    args[2].write(this, 0)
                }
                PV(pointer + opAndModes.op, Value.None)
            }
            is Operation.Term -> null
        }
    }

}

