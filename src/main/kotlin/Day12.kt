import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * --- Rain Risk ---
 * https://adventofcode.com/2020/day/12
 */
fun main() {
    val pattern = "([NSEWLRF])(\\d+)$".toRegex(RegexOption.MULTILINE)
    val input = ResourceReader(12).text().let {
        pattern.findAll(it)
    }.map { it.destructured }.map {
        (char, int) -> Action(char[0], int.toInt())
    }.toList()

    println(part1(input))
    println(part2(input))
}

private fun part1(input : List<Action>) : Int{
    var direction = Direction.E
    var x = 0
    var y = 0
    input.forEach {
        when(it){
            is Action.N -> y += it.value
            is Action.S -> y -= it.value
            is Action.E -> x += it.value
            is Action.W -> x -= it.value
            is Action.L, is Action.R -> direction += it
            is Action.F -> when(direction){
                Direction.E -> x += it.value
                Direction.W -> x -= it.value
                Direction.N -> y += it.value
                Direction.S -> y -= it.value
            }
        }
    }
    return abs(x) + abs(y)
}

private fun part2(input : List<Action>) : Int {
    val waypoint = object {
        var x = 10
        var y = 1
        operator fun plusAssign(action: Action){
            when(action){
                is Action.N -> y += action.value
                is Action.S -> y -= action.value
                is Action.E -> x += action.value
                is Action.W -> x -= action.value
                is Action.L -> this += Action.R(-action.value)
                is Action.R -> {
                    val rad = action.value.toRadians()
                    val s = sin(rad).toInt()
                    val c = cos(rad).toInt()
                    val newx = x * c + y * s
                    val newy = -x * s + y * c
                    x = newx
                    y = newy
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    val ship = object {
        var x = 0
        var y = 0
        fun manhattanDistance() : Int = abs(x) + abs(y)
    }

    input.forEach {
        if(it is Action.F){
            ship.x += waypoint.x * it.value
            ship.y += waypoint.y * it.value
        } else {
            waypoint += it
        }
    }
    return ship.manhattanDistance()
}

fun Int.toRadians() : Double = Math.toRadians(toDouble())

private enum class Direction{
    N,E,S,W;
    operator fun plus(action : Action) : Direction{
       return when(action){
            is Action.L -> ((ordinal + 4 - action.value/90) % 4).let { values()[it]}
            is Action.R -> ((ordinal + action.value/90) % 4).let { values()[it]}
            else -> this
        }
    }
}
private sealed class Action(open val value : Int){
    data class N(override val value: Int) : Action(value)
    data class S(override val value : Int) : Action(value)
    data class E(override val value : Int) : Action(value)
    data class W(override val value : Int) :Action(value)
    data class L(override val value : Int) : Action(value)
    data class R(override val value : Int) : Action(value)
    data class F(override val value : Int) : Action(value)
    companion object{
        operator fun invoke(char: Char, int : Int) : Action{
            return when(char){
                'N' -> N(int)
                'S' -> S(int)
                'E' -> E(int)
                'W' -> W(int)
                'L' -> L(int)
                'R' -> R(int)
                'F' -> F(int)
                else -> throw IllegalArgumentException()
            }
        }
    }

}