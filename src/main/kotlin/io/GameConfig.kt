package io

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
)