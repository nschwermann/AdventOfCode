fun main() {
    val ints = ResourceReader(1).ints()
    println(part1(ints, 2020))
    println(part2(ints, 2020))
}

//Find the two ints who sum is 2020 and return their product
fun part1(ints : List<Int>, sum : Int) : Int {
    for(i in ints.indices){
        val value = ints[i]
        val want = sum - value
        if(ints.contains(want)) return value * want
    }
    return 0
}

//Find THREE ints who sum is 2020 and return their product.
fun part2(ints : List<Int>, sum : Int) : Int {
    for(i in ints.indices){
        val value = ints[i]
        val remains = sum - value
        val pairdown = ints.filter { it <= remains }
        val next = part1(pairdown, remains)
        if(next != 0) return next * value
    }
    return 0
}