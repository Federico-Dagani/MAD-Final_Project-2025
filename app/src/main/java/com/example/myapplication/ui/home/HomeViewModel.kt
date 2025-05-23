package com.example.myapplication.ui.home

import androidx.lifecycle.*
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val db: AppDatabase) : ViewModel() {

    private val _rooms = MutableLiveData<List<RoomEntity>>()
    val rooms: LiveData<List<RoomEntity>> = _rooms

    fun loadAllRooms(roomType: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            var roomList = db.roomDao().getAllRooms()
            if (roomType != null) {
                roomList = roomList.filter { it.type == roomType }
            }
            _rooms.postValue(roomList)
        }
    }

    fun loadAvailableRooms(roomType: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = System.currentTimeMillis()
            var availableRooms = db.eventDao().getAvailableRooms(currentTime)
            if (roomType != null) {
                availableRooms = availableRooms.filter { it.type == roomType }
            }
            _rooms.postValue(availableRooms)
        }
    }

    private val _isFilteringAvailable = MutableLiveData<Boolean>(false)
    val isFilteringAvailable: LiveData<Boolean> = _isFilteringAvailable

    private val _selectedRoomType = MutableLiveData<String?>(null)
    val selectedRoomType: LiveData<String?> = _selectedRoomType

    fun setFilteringAvailable(filter: Boolean) {
        _isFilteringAvailable.value = filter
        updateRoomList()
    }

    fun setSelectedRoomType(type: String?) {
        _selectedRoomType.value = type
        updateRoomList()
    }

    private fun updateRoomList() {
        val type = _selectedRoomType.value
        val filter = _isFilteringAvailable.value ?: false
        if (filter) {
            loadAvailableRooms(type)
        } else {
            loadAllRooms(type)
        }
    }


}
