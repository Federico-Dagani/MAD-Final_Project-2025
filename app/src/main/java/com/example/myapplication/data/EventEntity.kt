package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = RoomEntity::class,
        parentColumns = ["roomId"],
        childColumns = ["roomId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val eventId: Int = 0,
    val roomId: String,
    val startTime: Long,
    val endTime: Long
)
