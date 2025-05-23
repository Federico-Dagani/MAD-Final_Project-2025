package com.example.myapplication.data
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class, EventEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun eventDao(): EventDao
}
