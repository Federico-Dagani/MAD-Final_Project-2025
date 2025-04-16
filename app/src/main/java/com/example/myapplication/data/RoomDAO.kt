package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Query

@Dao

//Show all rooms
interface RoomDao {
    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>
}