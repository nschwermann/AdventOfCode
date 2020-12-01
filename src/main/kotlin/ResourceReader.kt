class ResourceReader(private val day : Int) {

    fun text() : String = javaClass.getResource("/Day${day.toString().padStart(2, '0')}").readText()
    fun ints() = text().lines().map { it.toInt() }
    fun lines() = text().lines()
    fun intArray() = text().split(',').map { it.toInt() }.toIntArray()
}