//package viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import data.model.PriceReport
//import data.model.api.ApiClient
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class DetailViewModel() : ViewModel() {
//    private val api = ApiClient()
//    private var date: String = ""
//    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
//    val uiState: StateFlow<DetailUiState> = _uiState
////    init {
////        loadReport()
////    }
//    fun setDate(value: String){
//    println("🔴 DetailViewModel: setDate called with: '$value'")
//    date = value
//        loadReport()
//    }
//
//    fun loadReport() {
//        println("🔴 DetailViewModel: loadReport() called, date='$date'")
//        if (date.isBlank()){
//            println("🔴 DetailViewModel: Date is blank, returning error")
//            _uiState.value = DetailUiState.Error("Date is required")
//            return
//        }
//        viewModelScope.launch {
//            _uiState.value = DetailUiState.Loading
//            try {
//                val report = api.getReport(date)
//                println("🔴 DetailViewModel: API SUCCESS: $report")
//                _uiState.value = DetailUiState.Success(report)
//            } catch (e: Exception) {
//                println("🔴 DetailViewModel: API ERROR: ${e.message}")
//                e.printStackTrace()
//                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//}
//
//sealed class DetailUiState {
//    object Loading : DetailUiState()
//    data class Success(val report: PriceReport) : DetailUiState()
//    data class Error(val message: String) : DetailUiState()
//}

package viewmodel


import androidx.lifecycle.viewModelScope
import data.model.PriceReport
import data.model.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class DetailViewModel : ViewModel() {
    private val api = ApiClient()
    private var date: String = ""

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    fun setDate(value: String) {
        println("🔴 DetailViewModel: setDate called with: '$value'")
        date = value
        loadReport()
    }

    fun loadReport() {
        println("🔴 DetailViewModel: loadReport() called, date='$date'")
        if (date.isBlank()) {
            println("🔴 DetailViewModel: Date is blank, returning error")
            _uiState.value = DetailUiState.Error("Date is required")
            return
        }
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val report = api.getReport(date)
                println("🔴 DetailViewModel: API SUCCESS: sections=${report.data.sections.map { it.sectionName }}")
                println("🔴 DetailViewModel: vegetables=${report.data.vegetables?.size}, rice=${report.data.rice?.size}, fish=${report.data.fish?.size}, other=${report.data.other?.size}")
                _uiState.value = DetailUiState.Success(report)
            } catch (e: Exception) {
                println("🔴 DetailViewModel: API ERROR: ${e.message}")
                e.printStackTrace()
                _uiState.value = DetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val report: PriceReport) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}