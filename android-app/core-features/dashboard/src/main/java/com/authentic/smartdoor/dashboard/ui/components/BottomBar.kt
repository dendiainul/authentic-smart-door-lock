package com.authentic.smartdoor.dashboard.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.authentic.smartdoor.dashboard.ui.DashboardScreen

@Composable
fun BottomBar(
    onHomeClick: () -> Unit,
    onAccessHistoryClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    currentScreen: DashboardScreen = DashboardScreen.Home
) {
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
                // Home Icon
                IconButton(
                    onClick = onHomeClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (currentScreen is DashboardScreen.Home) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Access History Icon
                IconButton(
                    onClick = onAccessHistoryClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (currentScreen is DashboardScreen.AccessHistory) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = "Access History",
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = "Access History",
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Notifications Icon
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (currentScreen is DashboardScreen.Notifications) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Analytics Icon
                IconButton(
                    onClick = onAnalyticsClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (currentScreen is DashboardScreen.Analytics) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.BarChart,
                                contentDescription = "Analytics",
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = "Analytics",
                            tint = Color(0xFF6C63FF),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

