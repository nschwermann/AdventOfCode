import org.junit.Assert.*
import org.junit.Test

class HandHeldVMTest{
    private val testInput = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent()

    @Test
    fun testRunWithHalt(){
        val testProgram = CodeParser().parseCode(testInput)
        assertEquals(5, HandHeldVM().runWithHalt(testProgram))
    }

    @Test
    fun testWithRepair(){
        val testProgram = CodeParser().parseCode(testInput)
        assertEquals(8, HandHeldVM().runRepair(testProgram))
    }
}

