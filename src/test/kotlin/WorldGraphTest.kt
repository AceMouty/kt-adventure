import io.Connection
import io.Direction
import io.GameConfig
import io.Room
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorldGraphTest {
    private lateinit var world: WorldGraph

    @BeforeAll
    fun init() {
        world = Fixtures.worldGraph()
    }

    @Test
    fun `room is returned when room exists`() {
        val room = world.room("porch")
        assertEquals("porch", room.id)
    }

    @Test
    fun `room throws when id missing`() {
        assertThrows<IllegalStateException> {
            world.room("does-not-exist")
        }
    }

    @Test
    fun `nextRoomId returns null when no connection`() {
        assertNull(world.nextRoomId("A", Direction.EAST))
    }

    @Test
    fun `fromGameConfig wires connections correctly`() {
        val cfg = Fixtures.gameConfig("fixtures/basic-game.json")
        val graph = WorldGraph.fromGameConfig(cfg)

        // room A connections
        assertEquals("B", graph.nextRoomId("A", Direction.SOUTH))
        assertEquals("C", graph.nextRoomId("A", Direction.WEST))
        // room B connections
        assertEquals("A", graph.nextRoomId("B", Direction.NORTH))
        // room C connections
        assertEquals("A", graph.nextRoomId("C", Direction.NORTH))
    }

    @Test
    fun `fromGameConfig errors when connection_to references missing room`() {
        val badCfg = Fixtures.gameConfig("fixtures/broken-connections-game.json")

        assertThrows<IllegalStateException> {
            WorldGraph.fromGameConfig(badCfg)
        }
    }

    @Test
    fun `fromGameConfig errors when connection_from references missing room`() {
        val badCfg = GameConfig(
            startRoom = "A",
            rooms = listOf(
                Room("B", name = "Room B", description = "Room B")
            ),
            connections = listOf(
                Connection(from = "A", to = "B", direction = Direction.SOUTH),
            )
        )

        assertThrows<IllegalStateException> {
            WorldGraph.fromGameConfig(badCfg)
        }
    }

    @Test
    fun `fromGameConfig rejects duplicate room ids`() {
        val dupCfg = Fixtures.gameConfig("fixtures/duplicate-room-ids.json")

        assertThrows<IllegalArgumentException> {
            WorldGraph.fromGameConfig(dupCfg)
        }
    }
}