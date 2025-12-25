import io.GameConfig
import kotlinx.serialization.json.Json

object Fixtures {
    private val json = Json { ignoreUnknownKeys = true }

    fun readTestResource(path: String): String =
        requireNotNull(Thread.currentThread().contextClassLoader.getResourceAsStream(path)) {
            "Missing test resource: $path"
        }.bufferedReader().use { it.readText() }

    fun gameConfig(path: String = "fixtures/test-game.json"): GameConfig =
        json.decodeFromString(GameConfig.serializer(), readTestResource(path))

    fun worldGraph(path: String = "fixtures/test-game.json"): WorldGraph =
        WorldGraph.fromGameConfig(gameConfig(path))

    fun session(path: String = "fixtures/test-game.json"): GameSession =
        GameSession.fromGameConfig(gameConfig(path))
}