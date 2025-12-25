import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ReplCommandTest {
    @Test
    fun `parse recognizes aliases`() {
        assertEquals(ReplCommand.NORTH, ReplCommand.parse("n"))
        assertEquals(ReplCommand.NORTH, ReplCommand.parse("north"))
        assertEquals(ReplCommand.INVENTORY, ReplCommand.parse("inv"))
        // case-insensitive
        assertEquals(ReplCommand.INVENTORY, ReplCommand.parse("I"))
    }

    @Test
    fun `parse returns null for unknown command`() {
        assertNull(ReplCommand.parse("unknown"))
        assertNull(ReplCommand.parse("asdf"))
        assertNull(ReplCommand.parse(""))
    }

    @Test
    fun `showHelp contains each command usage`() {
        val help = ReplCommand.showHelp()

        for (cmd in ReplCommand.entries) {
            assertTrue(
                help.contains(cmd.usage),
                "Help text missing usage for ${cmd.name}: '${cmd.usage}'"
            )
        }
    }
}