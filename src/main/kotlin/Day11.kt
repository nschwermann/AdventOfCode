/**
 * --- Seating System ---
 * https://adventofcode.com/2020/day/11
 */
private fun main() {

    val input = ResourceReader(11).text().lines().let { lines ->
        Array(lines.size){ index ->
            lines[index].map { SeatState(it) }.toTypedArray()
        }
    }
    println(part1(Array(input.size){
        input[it].copyOf()
    }))
    println(part2(input))
}

private fun part1(input : Array<Array<SeatState>>) : Int{
    return changePlaces(input,
            {row, col -> input[row][col] == SeatState.Empty && input.countAdjacentOccupied(row, col) == 0},
            {row, col -> input[row][col] == SeatState.Occupied && input.countAdjacentOccupied(row, col) >= 4}
    )
}

private fun part2(input : Array<Array<SeatState>>) : Int {
    return changePlaces(input,
            {row, col -> input[row][col] == SeatState.Empty && input.countSeenOccupied(row, col) == 0},
            {row, col -> input[row][col] == SeatState.Occupied && input.countSeenOccupied(row, col) >= 5}
    )
}

private fun changePlaces(input: Array<Array<SeatState>>,
                         emptyCheck : (Int, Int) -> Boolean,
                         occupiedCheck : (Int, Int) -> Boolean) : Int {
    var round = 0
    val buffer = Array(input.size){
        input[it].copyOf()
    }
    while(true){
        var changes = 0
        for(row in input.indices){
            for(col in input.first().indices){
                if(emptyCheck(row, col)) {
                    buffer[row][col] = SeatState.Occupied
                    changes++
                }
                else if(occupiedCheck(row, col)) {
                    buffer[row][col] = SeatState.Empty
                    changes++
                }
            }
        }
        round++
        if(changes == 0) break

        buffer.forEachIndexed { index, arrayOfSeatStates ->
            arrayOfSeatStates.copyInto(input[index])
        }
    }
    return input.flatMap { it.toList() }.count{ it == SeatState.Occupied }
}

private fun Array<Array<SeatState>>.print(){
    forEach { it.forEach { print(it) }; println() }
}


//[row][col]
private fun Array<Array<SeatState>>.countAdjacentOccupied(row : Int, col : Int) : Int{
    var occupied = 0
    for(r in row - 1 .. row + 1){
        for(c in col -1 .. col + 1){
            if(c == col && r == row) continue
            if(getOrNull(r)?.getOrNull(c) == SeatState.Occupied) occupied++
        }
    }
    return occupied
}

private fun Array<Array<SeatState>>.countSeenOccupied(row : Int, col : Int) : Int {
    var seen = 0
    fun look(row: Int, col : Int) :SeatState = getOrNull(row)?.getOrNull(col) ?: SeatState.Floor

    for(r in row-1 downTo 0){
        val saw = look(r, col)
        if(saw == SeatState.Empty) break
        else if(saw == SeatState.Occupied){
            seen++
            break
        }
    }
    for(r in row+1 until size){
        val saw = look(r, col)
        if(saw == SeatState.Empty) break
        else if(saw == SeatState.Occupied){
            seen++
            break
        }
    }
    for(c in col-1 downTo 0){
        val saw = look(row, c)
        if(saw == SeatState.Empty) break
        else if(saw == SeatState.Occupied){
            seen++
            break
        }
    }
    for(c in col+1 until get(row).size){
        val saw = look(row, c)
        if(saw == SeatState.Empty) break
        else if(saw == SeatState.Occupied){
            seen++
            break
        }
    }

    ((row-1 downTo 0).iterator() to (col-1 downTo 0).iterator()).let{ (r,c) ->
        while(r.hasNext() && c.hasNext()){
            val saw = look(r.next(), c.next())
            if(saw == SeatState.Empty) break
            else if(saw == SeatState.Occupied){
                seen++
                break
            }
        }
    }

    ((row+1 until size).iterator() to (col-1 downTo 0).iterator()).let{ (r,c) ->
        while(r.hasNext() && c.hasNext()){
            val saw = look(r.next(), c.next())
            if(saw == SeatState.Empty) break
            else if(saw == SeatState.Occupied){
                seen++
                break
            }
        }
    }

    ((row-1 downTo 0).iterator() to (col+1 until get(row).size).iterator()).let{ (r,c) ->
        while(r.hasNext() && c.hasNext()){
            val saw = look(r.next(), c.next())
            if(saw == SeatState.Empty) break
            else if(saw == SeatState.Occupied){
                seen++
                break
            }
        }
    }

    ((row+1 until size).iterator() to (col+1 until get(row).size).iterator()).let{ (r,c) ->
        while(r.hasNext() && c.hasNext()){
            val saw = look(r.next(), c.next())
            if(saw == SeatState.Empty) break
            else if(saw == SeatState.Occupied){
                seen++
                break
            }
        }
    }

    return seen
}

private enum class SeatState(val symbol : Char){
    Occupied('#'),
    Empty('L'),
    Floor('.');

    override fun toString(): String = symbol.toString()
    companion object{
        operator fun invoke(char: Char) : SeatState = when(char){
            '#' -> Occupied
            'L' -> Empty
            else -> Floor
        }
    }
}