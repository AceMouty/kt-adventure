import io.Direction
import io.GameConfig
import io.Room
import kotlinx.serialization.json.Json

class WorldGraph private constructor(
    private val rooms: Map<String, Room>,
) {

    fun room(id: String): Room =
        rooms[id] ?: error("Current '$id' does not exist.")

    fun nextRoomId(fromRoomId: String, dir: Direction): String? =
        rooms[fromRoomId]?.connections[dir]

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromGameConfig(gameConfig: GameConfig): WorldGraph {
            val rooms = mutableMapOf<String, Room>()

            for (r in gameConfig.rooms) {
                require(r.id !in rooms) { "${r.id} already in use." }
                rooms[r.id] = r
            }

            for(c in gameConfig.connections) {
                val from = rooms[c.from] ?: error("buildWorld::Unknown room in connection.from: ${c.from}")
                val to   = rooms[c.to]   ?: error("buildWorld::Unknown room in connection.to: ${c.to}")
                from.connect(c.direction, to)
            }

            return WorldGraph(rooms)
        }
    }
}