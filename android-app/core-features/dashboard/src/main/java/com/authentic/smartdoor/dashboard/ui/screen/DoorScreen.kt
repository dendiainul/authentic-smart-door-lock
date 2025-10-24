package com.authentic.smartdoor.dashboard.ui.screen

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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Battery0Bar
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
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
import com.authentic.smartdoor.dashboard.presentation.DashboardEvent
import com.authentic.smartdoor.dashboard.presentation.viewmodel.DashboardViewModel
import com.authentic.smartdoor.dashboard.ui.components.BottomBar
import com.authentic.smartdoor.dashboard.ui.components.DoorConfirmationModal
import com.authentic.smartdoor.dashboard.ui.DashboardScreen

@Composable
fun DoorScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToAccessHistory: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCameraLiveStream: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery = remember { mutableStateOf("") }
    
    // State for confirmation modal
    var showConfirmationModal by remember { mutableStateOf(false) }
    var selectedDoor by remember { mutableStateOf<com.authentic.smartdoor.dashboard.domain.model.Door?>(null) }
    var selectedAction by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(DashboardEvent.LoadDashboardData)
    }

    // SwipeRefresh state
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isRefreshing)

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF7F6FF),
        bottomBar = { 
            BottomBar(
                onHomeClick = { /* Already on home */ },
                onAccessHistoryClick = onNavigateToAccessHistory,
                onNotificationsClick = onNavigateToNotifications,
                onAnalyticsClick = onNavigateToAnalytics,
                currentScreen = DashboardScreen.Home
            ) 
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6C63FF))
            }
        } else {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.handleEvent(DashboardEvent.RefreshData) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF7F6FF))
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Header(
                        userName = uiState.user?.name ?: "Hafiz Atama",
                        unreadCount = uiState.unreadNotificationCount,
                        onProfileClick = onNavigateToProfile
                    )
                }

                item {
                    SearchBar(
                        query = searchQuery.value,
                        onQueryChange = { searchQuery.value = it }
                    )
                }

                // Door cards
                val filteredDoors = if (searchQuery.value.isBlank()) {
                    uiState.doors
                } else {
                    uiState.doors.filter {
                        it.name.contains(searchQuery.value, ignoreCase = true) ||
                                it.location.contains(searchQuery.value, ignoreCase = true)
                    }
                }

                items(filteredDoors) { door ->
                    DoorCard(
                        door = door,
                        onLockClick = {
                            selectedDoor = door
                            selectedAction = "kunci"
                            showConfirmationModal = true
                        },
                        onUnlockClick = {
                            selectedDoor = door
                            selectedAction = "buka"
                            showConfirmationModal = true
                        },
                        onCameraClick = { onNavigateToCameraLiveStream(door.name) }
                    )
                }


                item {
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
        }

        // Confirmation Modal
        DoorConfirmationModal(
            isVisible = showConfirmationModal,
            doorName = selectedDoor?.name ?: "",
            action = selectedAction,
            onConfirm = {
                selectedDoor?.let { door ->
                    // selectedAction is already "buka" or "kunci", keep as is for API
                    val apiAction = selectedAction // Keep "buka" and "kunci" as is
                    viewModel.handleEvent(DashboardEvent.ControlDoor(apiAction, door.id))
                }
                showConfirmationModal = false
                selectedDoor = null
                selectedAction = ""
            },
            onDismiss = {
                showConfirmationModal = false
                selectedDoor = null
                selectedAction = ""
            }
        )

        // Error message
        uiState.errorMessage?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE60023)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    userName: String,
    unreadCount: Int,
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Halo, $userName!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Selamat Datang di SecureDoor",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF6B6B6B),
                    fontSize = 14.sp
                )
            )
        }

        IconButton(
            onClick = onProfileClick,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8E6FF))
        ) {
            Text(
                text = userName.split(" ").take(2).joinToString("") { it.take(1).uppercase() },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF6C63FF)
            )
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(20.dp)
            )
        },
        placeholder = {
            Text(
                "Search",
                color = Color(0xFF9E9E9E),
                fontSize = 14.sp
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
private fun DoorCard(
    door: com.authentic.smartdoor.dashboard.domain.model.Door,
    onLockClick: () -> Unit,
    onUnlockClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    val statusColor = if (door.locked) Color(0xFFFF5252) else Color(0xFF4CAF50)
    val containerColor = Color.White
    val borderColor = if (door.locked) Color.Transparent else Color(0xFFB8E986)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (borderColor != Color.Transparent) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with camera status and connectivity badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (door.cameraActive) "Kamera Aktif" else "Kamera Tidak Aktif",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (door.cameraActive) Color(0xFF1A1A1A) else Color(0xFF9E9E9E)
                    )
                )

                // Connectivity badge - green when online, red when offline (battery 0 = offline)
                val isOnline = door.batteryLevel > 0 && door.wifiStrength != null && door.wifiStrength != "Offline"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isOnline) Color(0xFF4CAF50) else Color(0xFFFF5252))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = if (isOnline) "Online" else "Offline",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Illustration area (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8DAFF)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for illustration
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF6C63FF).copy(alpha = 0.3f)
                )

                // Fullscreen icon
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Fullscreen",
                        tint = Color(0xFF1A1A1A),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Door info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = door.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = door.location,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF6B6B6B),
                            fontSize = 13.sp
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (door.locked) "Status: Terkunci" else "Status: Terbuka",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF6B6B6B),
                            fontSize = 13.sp
                        )
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    // Battery and WiFi indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Battery indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = getBatteryIcon(door.batteryLevel),
                                contentDescription = "Battery Level",
                                tint = getBatteryColor(door.batteryLevel),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${door.batteryLevel}%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF6B6B6B),
                                    fontSize = 12.sp
                                )
                            )
                        }
                        
                        // WiFi indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = getWifiIcon(door.wifiStrength, door.batteryLevel),
                                contentDescription = "WiFi Strength",
                                tint = getWifiColor(door.wifiStrength, door.batteryLevel),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = getWifiText(door.wifiStrength, door.batteryLevel),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF6B6B6B),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionButton(
                    label = "Kunci",
                    icon = Icons.Filled.Lock,
                    onClick = onLockClick,
                    enabled = !door.locked,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    label = "Buka",
                    icon = Icons.Filled.LockOpen,
                    onClick = onUnlockClick,
                    enabled = door.locked,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    label = "Kamera",
                    icon = Icons.Default.CameraAlt,
                    onClick = onCameraClick,
                    enabled = door.cameraActive,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6C63FF),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = Color(0xFF9E9E9E)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}



@Composable
private fun BottomBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFEDE9FF))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.BarChart,
                        contentDescription = "Stats",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}


// Helper functions for battery and wifi indicators
private fun getBatteryIcon(batteryLevel: Int): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        batteryLevel == 0 -> Icons.Filled.Battery0Bar
        batteryLevel <= 15 -> Icons.Filled.Battery1Bar
        batteryLevel <= 30 -> Icons.Filled.Battery2Bar
        batteryLevel <= 45 -> Icons.Filled.Battery3Bar
        batteryLevel <= 60 -> Icons.Filled.Battery4Bar
        batteryLevel <= 75 -> Icons.Filled.Battery5Bar
        batteryLevel <= 90 -> Icons.Filled.Battery6Bar
        else -> Icons.Filled.BatteryFull
    }
}

private fun getBatteryColor(batteryLevel: Int): Color {
    return when {
        batteryLevel == 0 -> Color(0xFFFF5252) // Red for dead battery
        batteryLevel <= 20 -> Color(0xFFFF9800) // Orange for low battery
        else -> Color(0xFF4CAF50) // Green for good battery
    }
}

private fun getWifiIcon(wifiStrength: String?, batteryLevel: Int): androidx.compose.ui.graphics.vector.ImageVector {
    if (batteryLevel == 0) return Icons.Filled.WifiOff
    
    return when (wifiStrength?.lowercase()) {
        "offline" -> Icons.Filled.WifiOff
        "weak", "fair", "good", "excellent" -> Icons.Filled.Wifi
        else -> Icons.Filled.WifiOff
    }
}

private fun getWifiColor(wifiStrength: String?, batteryLevel: Int): Color {
    if (batteryLevel == 0) return Color(0xFFFF5252) // Red when battery is dead
    
    return when (wifiStrength?.lowercase()) {
        "offline" -> Color(0xFFFF5252) // Red
        "weak" -> Color(0xFFFF9800) // Orange
        "fair" -> Color(0xFFFFC107) // Yellow
        "good" -> Color(0xFF8BC34A) // Light green
        "excellent" -> Color(0xFF4CAF50) // Green
        else -> Color(0xFFFF5252) // Red for unknown
    }
}

private fun getWifiText(wifiStrength: String?, batteryLevel: Int): String {
    if (batteryLevel == 0) return "Offline"
    
    return when (wifiStrength?.lowercase()) {
        "offline" -> "Offline"
        "weak" -> "Lemah"
        "fair" -> "Sedang"
        "good" -> "Baik"
        "excellent" -> "Sangat Baik"
        else -> "Offline"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F6FF)
@Composable
private fun DoorScreenPreview() {
    DoorScreen()
}