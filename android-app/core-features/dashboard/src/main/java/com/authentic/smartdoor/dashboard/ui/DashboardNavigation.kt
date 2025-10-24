package com.authentic.smartdoor.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import com.authentic.smartdoor.dashboard.presentation.DashboardEvent
import com.authentic.smartdoor.dashboard.presentation.viewmodel.DashboardViewModel
import com.authentic.smartdoor.dashboard.ui.components.DoorConfirmationModal
import com.authentic.smartdoor.dashboard.ui.components.BottomBar
import com.authentic.smartdoor.dashboard.ui.screen.AccessHistoryScreen
import com.authentic.smartdoor.dashboard.ui.screen.AccessHistoryDetailScreen
import com.authentic.smartdoor.dashboard.ui.screen.AnalyticsScreen
import com.authentic.smartdoor.dashboard.ui.screen.CameraLiveStreamScreen
import com.authentic.smartdoor.dashboard.ui.screen.DoorScreen
import com.authentic.smartdoor.dashboard.ui.screen.NotificationScreen
import com.authentic.smartdoor.dashboard.ui.screen.NotificationSettingsScreen
import com.authentic.smartdoor.dashboard.ui.screen.ProfileScreen

sealed class DashboardScreen {
    object Home : DashboardScreen()
    object AccessHistory : DashboardScreen()
    data class AccessHistoryDetail(val accessLog: AccessLog) : DashboardScreen()
    object Notifications : DashboardScreen()
    object NotificationSettings : DashboardScreen()
    object Analytics : DashboardScreen()
    object Profile : DashboardScreen()
    object EditProfile : DashboardScreen()
    data class CameraLiveStream(val doorName: String) : DashboardScreen()
}

@Composable
fun DashboardNavigation(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    var currentScreen by remember { mutableStateOf<DashboardScreen>(DashboardScreen.Home) }
    
    when (currentScreen) {
        is DashboardScreen.Home -> {
            DoorScreen(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToAccessHistory = { currentScreen = DashboardScreen.AccessHistory },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications },
                onNavigateToProfile = { currentScreen = DashboardScreen.Profile },
                onNavigateToCameraLiveStream = { doorName -> 
                    currentScreen = DashboardScreen.CameraLiveStream(doorName) 
                }
            )
        }
        is DashboardScreen.AccessHistory -> {
            AccessHistoryScreen(
                modifier = modifier,
                onBackClick = { currentScreen = DashboardScreen.Home },
                onNavigateToHome = { currentScreen = DashboardScreen.Home },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications },
                onNavigateToDetail = { accessLog -> 
                    currentScreen = DashboardScreen.AccessHistoryDetail(accessLog) 
                }
            )
        }
        is DashboardScreen.AccessHistoryDetail -> {
            val accessLog = (currentScreen as DashboardScreen.AccessHistoryDetail).accessLog
            AccessHistoryDetailScreen(
                accessLog = accessLog,
                onBackClick = { currentScreen = DashboardScreen.AccessHistory },
                onViewCamera = { 
                    // TODO: Navigate to camera view for this specific access log
                    // For now, just go back to access history
                    currentScreen = DashboardScreen.AccessHistory
                }
            )
        }
        is DashboardScreen.Notifications -> {
            Scaffold(
                modifier = modifier,
                containerColor = Color.White,
                bottomBar = { 
                    BottomBar(
                        onHomeClick = { currentScreen = DashboardScreen.Home },
                        onAccessHistoryClick = { currentScreen = DashboardScreen.AccessHistory },
                        onNotificationsClick = { currentScreen = DashboardScreen.Notifications },
                        onAnalyticsClick = { currentScreen = DashboardScreen.Analytics },
                        currentScreen = DashboardScreen.Notifications
                    ) 
                }
            ) { padding ->
                NotificationScreen(
                    modifier = Modifier.padding(padding),
                    onBackClick = { currentScreen = DashboardScreen.Home },
                    onNavigateToHome = { currentScreen = DashboardScreen.Home },
                    onNavigateToAccessHistory = { currentScreen = DashboardScreen.AccessHistory },
                    onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                    onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications }
                )
            }
        }
        is DashboardScreen.NotificationSettings -> {
            NotificationSettingsScreen(
                modifier = modifier,
                onBackClick = { currentScreen = DashboardScreen.Profile },
                onNavigateToHome = { currentScreen = DashboardScreen.Home },
                onNavigateToAccessHistory = { currentScreen = DashboardScreen.AccessHistory },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications }
            )
        }
        is DashboardScreen.Analytics -> {
            AnalyticsScreen(
                modifier = modifier,
                onBackClick = { currentScreen = DashboardScreen.Home },
                onNavigateToHome = { currentScreen = DashboardScreen.Home },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications }
            )
        }
        is DashboardScreen.Profile -> {
            ProfileScreen(
                modifier = modifier,
                onBackClick = { currentScreen = DashboardScreen.Home },
                onNavigateToHome = { currentScreen = DashboardScreen.Home },
                onNavigateToAccessHistory = { currentScreen = DashboardScreen.AccessHistory },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications },
                onNavigateToNotificationSettings = { currentScreen = DashboardScreen.NotificationSettings },
                onNavigateToEditProfile = { currentScreen = DashboardScreen.EditProfile }
            )
        }
        is DashboardScreen.EditProfile -> {
            // EditProfile screen not implemented yet
            ProfileScreen(
                modifier = modifier,
                onBackClick = { currentScreen = DashboardScreen.Home },
                onNavigateToHome = { currentScreen = DashboardScreen.Home },
                onNavigateToAccessHistory = { currentScreen = DashboardScreen.AccessHistory },
                onNavigateToAnalytics = { currentScreen = DashboardScreen.Analytics },
                onNavigateToNotifications = { currentScreen = DashboardScreen.Notifications },
                onNavigateToNotificationSettings = { currentScreen = DashboardScreen.NotificationSettings },
                onNavigateToEditProfile = { currentScreen = DashboardScreen.EditProfile }
            )
        }
        is DashboardScreen.CameraLiveStream -> {
            val doorName = (currentScreen as DashboardScreen.CameraLiveStream).doorName
            CameraLiveStreamScreen(
                doorName = doorName,
                onBackClick = { currentScreen = DashboardScreen.Home }
            )
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFFF7F6FF)
@Composable
private fun DashboardNavigationPreview() {
    DashboardNavigation()
}
