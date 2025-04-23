package com.example.myapplication.ui.home

import androidx.lifecycle.*
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val db: AppDatabase) : ViewModel() {

    private val _rooms = MutableLiveData<List<RoomEntity>>()
    val rooms: LiveData<List<RoomEntity>> = _rooms

    fun loadAllRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            val roomList = db.roomDao().getAllRooms()
            _rooms.postValue(roomList)
        }
    }

    fun loadAvailableRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = System.currentTimeMillis()
            val availableRooms = db.eventDao().getAvailableRooms(currentTime)
            _rooms.postValue(availableRooms)
        }
    }
}
