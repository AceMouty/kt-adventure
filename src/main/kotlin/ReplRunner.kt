import io.Direction

class ReplRunner(private val session: GameSession) {
    fun run() {
        println(renderPrompt())

        while(true) {
            print("> ")
            val line = readlnOrNull()?.trim() ?: break
            if(line.isBlank()) continue

            val result = handle(line)
            if(result == QUIT) break
            if(result.isNotBlank()) println(result)
        }

        println("Bye!")
    }

    private companion object {
        private const val QUIT = "__QUIT__"
    }

    fun renderPrompt(): String {
        val r = session.currentRoom
        val exits = if(r.connections.isEmpty()) "none" else r.connections.keys.joinToString(", ")
        val items = r.items?.joinToString(", ") ?: ""

        return buildString {
            appendLine("=== ${r.id}===")
            appendLine("description: ${r.description}")
            appendLine("Exits: $exits")
            appendLine("Items: $items")
        }.trimEnd()
    }

    fun handle(input: String): String {
        val parts = input.split(Regex("\\s+"))
        val cmd = parts[0].lowercase()
        val arg = parts.drop(1).joinToString(" ")

        return when(cmd) {
            "quit", "exit" -> QUIT
            "n", "north" -> move(Direction.NORTH)
            "s", "south" -> move(Direction.SOUTH)
            "e", "east" -> move(Direction.EAST)
            "w", "west" -> move(Direction.WEST)
            "take", "get" -> {
                if(arg.isBlank()) "what would you like to take?"
                else session.take(arg)?.let { "$it was added to inventory" } ?: "No such item here"
            }
            "inv", "inventory" -> {
                val inv = session.player.inventory
                if(inv.isEmpty()) "Inventory is empty"
                else inv.joinToString(prefix = "You have: ") { it }

            }
            else -> "Unknown command: $cmd"
        }
    }

    fun move(dir: Direction): String {
        return if(session.tryMove(dir)) renderPrompt()
        else "You cant move that way"
    }
}