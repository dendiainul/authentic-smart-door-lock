package com.authentic.smartdoor.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authentic.smartdoor.dashboard.domain.model.AnalyticsData
import com.authentic.smartdoor.dashboard.domain.model.AvailableDoor
import com.authentic.smartdoor.dashboard.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val analyticsData: AnalyticsData? = null,
    val availableDoors: List<AvailableDoor> = emptyList(),
    val selectedDoorId: Int? = null,
    val selectedDoorName: String = "Semua Pintu",
    val errorMessage: String? = null
)

sealed class AnalyticsEvent {
    object LoadAnalyticsData : AnalyticsEvent()
    object RefreshAnalyticsData : AnalyticsEvent()
    data class SelectDoor(val doorId: Int?, val doorName: String) : AnalyticsEvent()
    data class FilterByDateRange(val startDate: String?, val endDate: String?) : AnalyticsEvent()
    object ClearError : AnalyticsEvent()
}

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    fun handleEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.LoadAnalyticsData -> loadAnalyticsData()
            is AnalyticsEvent.RefreshAnalyticsData -> refreshAnalyticsData()
            is AnalyticsEvent.SelectDoor -> selectDoor(event.doorId, event.doorName)
            is AnalyticsEvent.FilterByDateRange -> filterByDateRange(event.startDate, event.endDate)
            is AnalyticsEvent.ClearError -> clearError()
        }
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                val result = repository.getAnalyticsData(_uiState.value.selectedDoorId)
                result.fold(
                    onSuccess = { analyticsData ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            analyticsData = analyticsData,
                            availableDoors = analyticsData.availableDoors
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to load analytics data"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    private fun refreshAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            
            try {
                val result = repository.getAnalyticsData(_uiState.value.selectedDoorId)
                result.fold(
                    onSuccess = { analyticsData ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            analyticsData = analyticsData,
                            availableDoors = analyticsData.availableDoors
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            errorMessage = exception.message ?: "Failed to refresh analytics data"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    private fun selectDoor(doorId: Int?, doorName: String) {
        _uiState.value = _uiState.value.copy(
            selectedDoorId = doorId,
            selectedDoorName = doorName
        )
        loadAnalyticsDataForSelectedDoor() // Reload data with new filter
    }

    private fun filterByDateRange(startDate: String?, endDate: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                val result = repository.getAnalyticsData(_uiState.value.selectedDoorId, startDate, endDate)
                result.fold(
                    onSuccess = { analyticsData ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            analyticsData = analyticsData
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to filter analytics data"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    private fun loadAnalyticsDataForSelectedDoor() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                val result = repository.getAnalyticsData(_uiState.value.selectedDoorId)
                result.fold(
                    onSuccess = { analyticsData ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            analyticsData = analyticsData
                            // Jangan update availableDoors di sini, biarkan tetap sama
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to load analytics data"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
