package com.authentic.smartdoor.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authentic.smartdoor.dashboard.domain.repository.DashboardRepository
import com.authentic.smartdoor.dashboard.presentation.DashboardEvent
import com.authentic.smartdoor.dashboard.presentation.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun handleEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadDashboardData -> loadDashboardData()
            is DashboardEvent.RefreshData -> refreshData()
            is DashboardEvent.ControlDoor -> controlDoor(event.action, event.doorId)
            is DashboardEvent.MarkNotificationsAsRead -> markNotificationsAsRead(event.notificationIds)
            is DashboardEvent.ClearError -> clearError()
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            dashboardRepository.getDashboardData()
                .fold(
                    onSuccess = { dashboardData ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = dashboardData.user,
                            doors = dashboardData.doors,
                            notifications = dashboardData.notifications,
                            recentAccessLogs = dashboardData.recentAccessLogs,
                            systemStatus = dashboardData.systemStatus,
                            errorMessage = null
                        )
                        
                        // Load unread notification count
                        loadUnreadNotificationCount()
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to load dashboard data"
                        )
                    }
                )
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            
            dashboardRepository.refreshData()
                .fold(
                    onSuccess = { dashboardData ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            user = dashboardData.user,
                            doors = dashboardData.doors,
                            notifications = dashboardData.notifications,
                            recentAccessLogs = dashboardData.recentAccessLogs,
                            systemStatus = dashboardData.systemStatus,
                            errorMessage = null
                        )
                        
                        // Load unread notification count
                        loadUnreadNotificationCount()
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            errorMessage = exception.message ?: "Failed to refresh data"
                        )
                    }
                )
        }
    }

    private fun controlDoor(action: String, doorId: String? = null) {
        viewModelScope.launch {
            val result = if (doorId != null) {
                dashboardRepository.controlDoorById(action, doorId)
            } else {
                dashboardRepository.controlDoor(action)
            }
            
            result.fold(
                onSuccess = { success ->
                    if (success) {
                        // Refresh all door statuses after successful control
                        loadDashboardData()
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Failed to control door"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Failed to control door"
                    )
                }
            )
        }
    }

    private fun markNotificationsAsRead(notificationIds: List<String>) {
        viewModelScope.launch {
            dashboardRepository.markNotificationsAsRead(notificationIds)
                .fold(
                    onSuccess = {
                        // Update local state
                        val updatedNotifications = _uiState.value.notifications.map { notification ->
                            if (notificationIds.contains(notification.id)) {
                                notification.copy(read = true)
                            } else {
                                notification
                            }
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            notifications = updatedNotifications,
                            unreadNotificationCount = updatedNotifications.count { !it.read }
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            errorMessage = exception.message ?: "Failed to mark notifications as read"
                        )
                    }
                )
        }
    }

    private fun loadUnreadNotificationCount() {
        viewModelScope.launch {
            dashboardRepository.getUnreadNotificationCount()
                .fold(
                    onSuccess = { count ->
                        _uiState.value = _uiState.value.copy(
                            unreadNotificationCount = count
                        )
                    },
                    onFailure = {
                        // Use local count if API fails
                        val localCount = _uiState.value.notifications.count { !it.read }
                        _uiState.value = _uiState.value.copy(
                            unreadNotificationCount = localCount
                        )
                    }
                )
        }
    }

    private fun refreshDoorStatus() {
        viewModelScope.launch {
            // Reload the entire dashboard data to get updated door statuses
            loadDashboardData()
        }
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
