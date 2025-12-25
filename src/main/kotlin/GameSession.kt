import io.Direction
import io.GameConfig
import io.Room

class GameSession private constructor(
    val worldGraph: WorldGraph,
    val player: Player,
){
    val currentRoom: Room
        get() = worldGraph.room(player.currentRoomId)

    fun tryMove(direction: Direction): Boolean {
        val nextRoomId = currentRoom.connections[direction] ?: return false
        player.currentRoomId = nextRoomId
        return true
    }

    fun take(token: String): String? {
        val idx = currentRoom.items?.indexOfFirst {
            it.equals(token, ignoreCase = true)
        } ?: return null

        if ( idx == -1 ) return null
        return currentRoom.items?.removeAt(idx)?.also { player.inventory.add(it) }
    }

    companion object {
        fun fromConfig(cfg: GameConfig): GameSession {
            val world = WorldGraph.fromConfig(cfg)
            val player = Player( currentRoomId = world.currentRoomId)

            return GameSession(world, player)
        }
    }
}