// RoomType.kt
enum class RoomType {
    CLASSROOM, MEETING_ROOM, LECTURE_HALL, CONFERENCE_ROOM, AUDITORIUM
}

// Room.kt
data class Room(
    val id: String,
    val type: RoomType,
    val title: String
)

// Event.kt
data class Event(
    val id: Long,
    val title: String,
    val startTime: Long,  // Stored as timestamp
    val endTime: Long,
    val roomId: String
)