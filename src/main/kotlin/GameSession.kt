import io.Direction
import io.GameConfig
import io.Room

class GameSession private constructor(
    val worldGraph: WorldGraph,
    val player: Player,
    var currentRoomId: String
){
    val currentRoom: Room
        get() = worldGraph.room(currentRoomId)

    fun tryMove(direction: Direction): Boolean =
        worldGraph.nextRoomId(currentRoomId, direction)
            ?.also { currentRoomId = it} != null

    fun take(token: String): String? {
        val idx = currentRoom.items?.indexOfFirst {
            it.equals(token, ignoreCase = true)
        } ?: return null

        if ( idx == -1 ) return null
        return currentRoom.items?.removeAt(idx)?.also { player.inventory.add(it) }
    }

    companion object {
        fun fromGameConfig(gameConfig: GameConfig): GameSession {
            val world = WorldGraph.fromGameConfig(gameConfig)
            return GameSession(
                world,
                Player(),
                gameConfig.startRoom
            )
        }
    }
}