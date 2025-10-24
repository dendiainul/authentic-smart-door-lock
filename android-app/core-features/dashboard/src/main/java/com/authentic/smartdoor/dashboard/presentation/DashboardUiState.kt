package com.authentic.smartdoor.dashboard.presentation

import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import com.authentic.smartdoor.dashboard.domain.model.Door
import com.authentic.smartdoor.dashboard.domain.model.Notification
import com.authentic.smartdoor.dashboard.domain.model.SystemStatus
import com.authentic.smartdoor.dashboard.domain.model.User

data class DashboardUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
    val doors: List<Door> = emptyList(),
    val notifications: List<Notification> = emptyList(),
    val recentAccessLogs: List<AccessLog> = emptyList(),
    val systemStatus: SystemStatus? = null,
    val unreadNotificationCount: Int = 0,
    val isRefreshing: Boolean = false
)

sealed class DashboardEvent {
    object LoadDashboardData : DashboardEvent()
    object RefreshData : DashboardEvent()
    data class ControlDoor(val action: String, val doorId: String? = null) : DashboardEvent()
    data class MarkNotificationsAsRead(val notificationIds: List<String>) : DashboardEvent()
    object ClearError : DashboardEvent()
}
