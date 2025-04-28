package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

data class MockData(
    val rooms: List<RoomEntity>,
    val events: List<EventEntity>
)

fun loadMockData(context: Context, db: AppDatabase) {
    CoroutineScope(Dispatchers.IO).launch {
        val inputStream = context.assets.open("mock_data.json")
        val reader = InputStreamReader(inputStream)
        val mockData: MockData = Gson().fromJson(reader, object : TypeToken<MockData>() {}.type)

        db.roomDao().apply {
            mockData.rooms.forEach { insertRoom(it) }
        }
        db.eventDao().apply {
            mockData.events.forEach { insertEvent(it) }
        }
    }
}
