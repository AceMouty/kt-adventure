package io
import kotlinx.serialization.Serializable

@Serializable
class Room(
    val id: String,
    val name: String,
    val description: String,
    val items: MutableList<String>? = null,
) {
    val connections: MutableMap<Direction, String> = mutableMapOf()

    fun connect(dir: Direction, to: Room) {
        connections[dir] = to.id
    }

    override fun toString(): String {
        val connSummary = connections.entries
            .joinToString(prefix = "{", postfix = "}") { (dir, roomName) -> "${dir.name}=${roomName}" }

        return "Room(id='$id', name='$name', description='$description', items=$items, connections=$connSummary)"
    }

}