import io.Direction
import io.GameConfig
import io.Room
import kotlinx.serialization.json.Json

class WorldGraph private constructor(
    private val rooms: Map<String, Room>,
    currentRoomId: String
) {
    var currentRoomId: String = currentRoomId
    private set

    val currentRoom: Room
        get() = rooms[currentRoomId]
            ?: error("Current room '$currentRoomId' does not exist.")

    fun room(id: String): Room =
        rooms[id] ?: error("Current '$id' does not exist.")

    fun move(dir: Direction): Boolean {
        val nextRoomId = currentRoom.connections[dir] ?: return false
        currentRoomId = nextRoomId
        return true
    }

    companion object {
        fun fromJsonString(jsonString: String): WorldGraph {
            val json = Json { ignoreUnknownKeys = true }
            val cfg = json.decodeFromString<GameConfig>(jsonString)
            return fromConfig(cfg)
        }

        fun fromConfig(gameConfig: GameConfig): WorldGraph {
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

            return WorldGraph(rooms, gameConfig.startRoom)
        }
    }
}