package io

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class Direction(val value: String) {
    @SerialName("north") NORTH("north"),
    @SerialName("south") SOUTH("south"),
    @SerialName("east") EAST("east"),
    @SerialName("west") WEST("west");
}

@Serializable
data class Connection(
    val to: String,
    val from: String,
    val direction: Direction,
)

@Serializable
data class GameConfig (
    val startRoom: String,
    val rooms: List<Room>,
    val connections: List<Connection>
){
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        fun fromJsonString(jsonString: String): GameConfig {
            return json.decodeFromString(serializer(), jsonString)
        }
    }
}