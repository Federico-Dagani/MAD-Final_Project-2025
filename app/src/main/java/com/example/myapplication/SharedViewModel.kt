import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.departures.Departure

class SharedViewModel : ViewModel() {
    //Departure Board
    private val _departures = MutableStateFlow<List<Departure>>(emptyList())
    val departures: StateFlow<List<Departure>> = _departures

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadDepartures(stationId: String) {
        viewModelScope.launch {
            try {
                // Your API call implementation here
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
    // Placeholders for future migrations

    // Home Fragment functionality will go here
    // Home Fragment functionality will go here
    private val _homeData = MutableStateFlow<String?>(null)
    val homeData: StateFlow<String?> = _homeData

    // Dashboard Fragment functionality will go here
    private val _dashboardData = MutableStateFlow<String?>(null)
    val dashboardData: StateFlow<String?> = _dashboardData

    // Notifications Fragment functionality will go here
    private val _notificationsData = MutableStateFlow<String?>(null)
    val notificationsData: StateFlow<String?> = _notificationsData
    // End Region




}

//    fun loadDepartures(stationId: String) {
//        viewModelScope.launch {
//            try {
//                // Your API call implementation here
//                // _departures.value = response
//            } catch (e: Exception) {
//                _errorMessage.value = "Error: ${e.message}"
//            }
//        }
//    }
//    // End Region
//
//    // Region: Placeholders for future migrations
//    // Home Fragment functionality will go here
//    private val _homeData = MutableStateFlow<String?>(null)
//    val homeData: StateFlow<String?> = _homeData
//
//    // Dashboard Fragment functionality will go here
//    private val _dashboardData = MutableStateFlow<String?>(null)
//    val dashboardData: StateFlow<String?> = _dashboardData
//
//    // Notifications Fragment functionality will go here
//    private val _notificationsData = MutableStateFlow<String?>(null)
//    val notificationsData: StateFlow<String?> = _notificationsData
//    // End Region
//
////    // Add more shared state and functions as needed
//}
//
//
//}