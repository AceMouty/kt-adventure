import io.Direction

class ReplRunner(private val session: GameSession) {
    fun run() {
        println(renderPrompt())

        while(true) {
            print("> ")
            val line = readlnOrNull()?.trim() ?: break
            if(line.isBlank()) continue

            val result = handle(line) ?: break
            if(result.isNotBlank()) println(result)
        }

        println("Bye!")
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

    fun handle(input: String): String? {
        val parts = input.split(Regex("\\s+"))
        val cmdToken = parts.getOrNull(0).orEmpty()
        val arg = parts.getOrNull(1).orEmpty()

        val cmd = ReplCommand.parse(cmdToken) ?: return "Unknown command: $cmdToken"

        return when(cmd) {
            ReplCommand.QUIT -> null
            ReplCommand.NORTH -> move(Direction.NORTH)
            ReplCommand.SOUTH -> move(Direction.SOUTH)
            ReplCommand.EAST -> move(Direction.EAST)
            ReplCommand.WEST -> move(Direction.WEST)
            ReplCommand.LOOK -> renderPrompt()
            ReplCommand.TAKE -> {
                if(arg.isBlank()) "what would you like to take?"
                else session.take(arg)?.let { "$it was added to inventory" } ?: "No such item here"
            }
            ReplCommand.INVENTORY -> {
                val inv = session.player.inventory
                if(inv.isEmpty()) "Inventory is empty"
                else inv.joinToString(prefix = "You have: ") { it }

            }
            ReplCommand.HELP -> ReplCommand.showHelp()
        }
    }

    fun move(dir: Direction): String {
        return if(session.tryMove(dir)) renderPrompt()
        else "You cant move that way"
    }
}