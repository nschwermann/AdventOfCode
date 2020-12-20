/**
 * https://adventofcode.com/2020/day/4
 * --- Passport Processing ---
 **/

private fun main() {
    val input = ResourceReader(4).text().split("\\n\\n".toRegex())
    println(part1(input))
    println(part2(input))
}

private fun part1(input : List<String>) : Int{
    val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    return input.count {
        requiredFields.fold(true){acc, next -> acc && it.contains(next)}
    }
}

private fun part2(input : List<String>) : Int{
    val patterns = listOf(
        "(byr:200[1|2])|(byr:19[2-9][0-9])".toRegex(),
        "(iyr:201[0-9])|(iyr:2020)".toRegex(),
        "(eyr:202[0-9])|(eyr:2030)".toRegex(),
        "(hgt:(1[5-8][0-9]|19[0-3])cm)|(hgt:[5-6][0-9]in)|(hgt:7[0-6]in)".toRegex(),
        "hcl:#[a-f0-9]{6}(?=\\s|\$)".toRegex(),
        "(ecl:amb|blu|brn|gry|grn|hzl|oth)".toRegex(),
        "pid:[0-9]{9}(?=\\s|\$)".toRegex()
    )

    return input.count {
        patterns.fold(true){acc, next ->
        acc && it.contains(next)}
    }
}