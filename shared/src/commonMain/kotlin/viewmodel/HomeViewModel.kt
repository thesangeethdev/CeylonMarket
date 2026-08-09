package viewmodel


import androidx.lifecycle.viewModelScope
import data.model.PriceReport
import data.model.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel: ViewModel() {

    private val api = ApiClient()
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState : StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val latest = api.getLatest()
                val reports = api.getReports()
                _uiState.value = HomeUiState.Success(latest, reports)
            }catch (e: Exception){
                _uiState.value = HomeUiState.Error(e.message?: "error")
            }
        }
    }
}

sealed class HomeUiState{
    object Loading: HomeUiState()
    data class Success(val latest: PriceReport, val recentReports: List<String>): HomeUiState()
    data class Error(val message: String): HomeUiState()
}