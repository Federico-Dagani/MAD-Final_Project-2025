import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.departures.Departure
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.DepartureBoardResponse
import retrofit2.Response

class SharedViewModel : ViewModel() {
    //Departure Board
    private val _departures = MutableStateFlow<List<Departure>>(emptyList())
    val departures: StateFlow<List<Departure>> = _departures

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadSingleStationDepartures(stationId: String) {
        viewModelScope.launch {
            _errorMessage.value = null // Reset error message before API call
            try {
                //Get API instance
                val api = ApiClient.instance
                //making API call
                println("DEBUG: Calling API with Station ID: $stationId")
                val response: Response<DepartureBoardResponse> =
                api.getDepartures(
                    accessId = "edb47416-fc62-4c53-af7d-b4e563f88ab7",//API KEY
                    id = stationId,
                )
                //Handle the response
                if (response.isSuccessful) {
                    _departures.value = response.body()?.DepartureBoard?.Departure ?: emptyList()
                } else {
                    _errorMessage.value = "API error: ${response.code()}"
                }

                // Your API call implementation here -> mock purposes
//                _departures.value = listOf(
//                    Departure("1", "10:00", "destination_station_mock", "1"))
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
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

//    fun loadSingleStationDepartures(stationId: String) {
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