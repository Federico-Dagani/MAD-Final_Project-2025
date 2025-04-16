package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EventDao {
    @Query("""
        SELECT * FROM events 
        WHERE roomId = :roomId AND 
              ((:currentTime BETWEEN startTime AND endTime))
    """)
    suspend fun getEventsForRoomAtTime(roomId: String, currentTime: Long): List<EventEntity>

    @Query("""
        SELECT * FROM rooms WHERE roomId NOT IN (
            SELECT roomId FROM events 
            WHERE :currentTime BETWEEN startTime AND endTime
        )
    """)
    suspend fun getAvailableRooms(currentTime: Long): List<RoomEntity>

    @Query("""
    SELECT * FROM rooms 
    WHERE floor = :floor AND roomId NOT IN (
        SELECT roomId FROM events 
        WHERE :currentTime BETWEEN startTime AND endTime
    )
""")
    suspend fun getAvailableRoomsOnFloor(floor: Int, currentTime: Long): List<RoomEntity>

    @Query("SELECT * FROM rooms WHERE floor = :floor")
    suspend fun getRoomsOnFloor(floor: Int): List<RoomEntity>



}