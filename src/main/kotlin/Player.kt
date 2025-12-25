data class Player(
    var currentRoomId: String,
    val inventory: MutableList<String> = mutableListOf(),
)