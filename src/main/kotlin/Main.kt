import io.GameConfig

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val jsonString = readResourceFile()
        val cfg = GameConfig.fromJsonString(jsonString)
        val gameSession = GameSession.fromGameConfig(cfg)

        ReplRunner(gameSession).run()
    }

    private fun readResourceFile(): String =
        Main::class.java
            .getResourceAsStream("/example-game.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("Could not load game file")

}