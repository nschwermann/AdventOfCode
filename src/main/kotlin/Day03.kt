import java.math.BigInteger

/**
 * https://adventofcode.com/2020/day/3
 * --- Toboggan Trajectory ---
 */
fun main() {
    val input = ResourceReader(3).lines().map {
        it.toCharArray()
    }.let { TobogganMap(it.size, it) }
    println(part1(input))
    println(part2(input))
}

private class TobogganMap(val height : Int, private val repeatingRows : List<CharArray>){

    fun getValue(row : Int, column : Int) : Char {
        val rowArray = repeatingRows[row]
        return rowArray[column % rowArray.size]
    }

    fun treesInTrajectory(moveRight : Int, moveDown : Int) : Int{
        val columns = (0 until Int.MAX_VALUE step moveRight).iterator()
        val rows = (0 until height step moveDown)
        var trees = 0
        for(row in rows){
            getValue(row, columns.nextInt()).also {
                if(it == '#') trees++
            }
        }
        return trees
    }
}

//Move right 3 and down 1 counting # till reach the bottom
private fun part1(matrix: TobogganMap) : Int {
    return matrix.treesInTrajectory(3, 1)
}

//Find following trajectories and multiply them together
//    Right 1, down 1.
//    Right 3, down 1.
//    Right 5, down 1.
//    Right 7, down 1.
//    Right 1, down 2.
private fun part2(matrix: TobogganMap) : BigInteger {
    val trajectories = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
    return trajectories.fold(1.toBigInteger()){ acc, (right, down) ->
        acc * matrix.treesInTrajectory(right, down).toBigInteger()
    }
}