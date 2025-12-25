import io.GameConfig
import io.Room
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val cfg = loadWorldConfig("example-game")
        val gameSession = GameSession.fromConfig(cfg)
        ReplRunner(gameSession).run()
    }

    private fun loadWorldConfig(fileName: String): GameConfig {
        val jsonString = readResourceFile(fileName)
        val json = Json { ignoreUnknownKeys = true }

        return json.decodeFromString(GameConfig.serializer(), jsonString)
    }

    private fun readResourceFile(fileName: String): String =
        Main::class.java
            .getResourceAsStream("/$fileName.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("Could not load game file")

}