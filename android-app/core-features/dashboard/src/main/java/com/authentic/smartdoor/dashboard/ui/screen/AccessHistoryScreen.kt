package com.authentic.smartdoor.dashboard.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.authentic.smartdoor.dashboard.presentation.viewmodel.AccessHistoryEvent
import com.authentic.smartdoor.dashboard.presentation.viewmodel.AccessHistoryViewModel
import com.authentic.smartdoor.dashboard.ui.components.BottomBar
import com.authentic.smartdoor.dashboard.ui.DashboardScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessHistoryScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToDetail: (AccessLog) -> Unit = {},
    viewModel: AccessHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(AccessHistoryEvent.LoadAccessHistory)
    }

    // SwipeRefresh state
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isRefreshing)

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF7F6FF),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Akses",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF6C63FF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F6FF)
                )
            )
        },
        bottomBar = { 
            BottomBar(
                onHomeClick = onNavigateToHome,
                onAccessHistoryClick = { /* Already on access history */ },
                onNotificationsClick = onNavigateToNotifications,
                onAnalyticsClick = onNavigateToAnalytics,
                currentScreen = DashboardScreen.AccessHistory
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
                onRefresh = { viewModel.handleEvent(AccessHistoryEvent.RefreshAccessHistory) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF7F6FF))
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                item {
                    Spacer(Modifier.height(8.dp))
                }

                items(uiState.accessLogs) { accessLog ->
                    AccessHistoryCard(
                        accessLog = accessLog,
                        onClick = { onNavigateToDetail(accessLog) }
                    )
                }

                item {
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
        }

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
private fun AccessHistoryCard(
    accessLog: com.authentic.smartdoor.dashboard.domain.model.AccessLog,
    onClick: () -> Unit
) {
    val (icon, iconColor, statusText) = when {
        accessLog.action == "buka" && accessLog.success -> {
            Triple(Icons.Default.LockOpen, Color(0xFF4FC3F7), "Berhasil Dibuka")
        }
        accessLog.action == "kunci" && accessLog.success -> {
            Triple(Icons.Default.Lock, Color(0xFF9C27B0), "Berhasil Dikunci")
        }
        !accessLog.success -> {
            Triple(Icons.Default.Warning, Color(0xFFFF5252), "Akses Ditolak")
        }
        else -> {
            Triple(Icons.Default.LockOpen, Color(0xFF4FC3F7), "Akses")
        }
    }

    val timeText = formatAccessTime(accessLog.timestamp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pintu ${accessLog.doorId}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        fontSize = 16.sp
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6B6B6B),
                        fontSize = 14.sp
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp
                    )
                )
            }

            // Arrow icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Details",
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun formatAccessTime(timestamp: String): String {
    return try {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
        formatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp)
        val now = System.currentTimeMillis()
        val diff = now - (date?.time ?: now)

        when {
            diff < 60000 -> "Baru saja"
            diff < 3600000 -> "${diff / 60000} menit lalu"
            diff < 86400000 -> {
                val hours = diff / 3600000
                if (hours < 24) "Hari ini, ${String.format("%02d:00", hours)}" else "Hari ini, ${String.format("%02d:00", hours % 24)}"
            }
            else -> {
                val days = diff / 86400000
                if (days == 1L) "Kemarin 23:00" else "${days} hari lalu"
            }
        }
    } catch (e: Exception) {
        "Waktu tidak diketahui"
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF7F6FF)
@Composable
private fun AccessHistoryScreenPreview() {
    AccessHistoryScreen()
}
