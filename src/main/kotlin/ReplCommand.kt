enum class ReplCommand(val usage: String, vararg aliases: String) {
    NORTH("north | n - move north", "north", "n"),
    SOUTH("south | s - move south", "south", "s"),
    EAST("east | e - move east","east", "e"),
    WEST("west | w - move west","west", "w"),

    LOOK("look | l - describe the current room","look", "l"),
    TAKE("[take,t] <item> - pickup an item","take", "t"),
    INVENTORY("inventory | inv | i - show inventory","inventory", "inv", "i"),
    HELP("help | h | ? - show help menu", "help", "h", "?"),
    QUIT("quit | exit | q - quit game","quit", "exit", "q");

    private val aliases: Set<String> = aliases.map { it.lowercase() }.toSet()

    fun matches(token: String): Boolean = token.lowercase() in aliases

    companion object {
        fun parse(token: String): ReplCommand? {
            val t = token.trim().lowercase()
            if(t.isEmpty()) return null

            return entries.firstOrNull { it.matches(t) }
        }

        fun showHelp(): String =
            entries.joinToString( separator = "\n") { it.usage }
    }
}