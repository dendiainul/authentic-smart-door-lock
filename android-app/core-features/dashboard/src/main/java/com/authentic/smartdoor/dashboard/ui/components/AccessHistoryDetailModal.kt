package com.authentic.smartdoor.dashboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessHistoryDetailModal(
    accessLog: AccessLog?,
    onDismiss: () -> Unit,
    onViewCamera: () -> Unit = {}
) {
    if (accessLog != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            // Full screen overlay with blur effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                // Modal content
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentSize()
                        .clickable { /* Prevent click from propagating to background */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    AccessHistoryDetailContent(
                        accessLog = accessLog,
                        onDismiss = onDismiss,
                        onViewCamera = onViewCamera
                    )
                }
            }
        }
    }
}

@Composable
private fun AccessHistoryDetailContent(
    accessLog: AccessLog,
    onDismiss: () -> Unit,
    onViewCamera: () -> Unit
) {
    val (icon, iconColor, statusText) = when {
        accessLog.action == "buka" && accessLog.success -> {
            Triple(Icons.Default.LockOpen, Color(0xFF4FC3F7), "Berhasil dibuka")
        }
        accessLog.action == "kunci" && accessLog.success -> {
            Triple(Icons.Default.Lock, Color(0xFF9C27B0), "Berhasil dikunci")
        }
        !accessLog.success -> {
            Triple(Icons.Default.Warning, Color(0xFFFF5252), "Akses ditolak")
        }
        else -> {
            Triple(Icons.Default.LockOpen, Color(0xFF4FC3F7), "Akses")
        }
    }

    val formattedTime = formatDetailTime(accessLog.timestamp)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(14.dp)
        ) {
            // Header with title and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detail Riwayat Akses",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF6B6B6B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Status icon and message
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Door icon representation
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = statusText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(Modifier.height(10.dp))

            // Details card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    DetailRow("Lokasi", "Pintu ${accessLog.doorId}")
                    DetailRow("Waktu", formattedTime)
                    DetailRow("Alasan", if (accessLog.success) "akses valid" else "akses tidak valid")
                    DetailRow("Percobaan ke", "1")
                    DetailRow("Status", if (accessLog.success) "Diterima" else "Ditolak")
                    DetailRow("Tindakan", statusText)
                    DetailRow("User", "User ${accessLog.userId}")
                    DetailRow("Metode", accessLog.method)
                    DetailRow("IP Address", accessLog.ipAddress)
                    DetailRow("Device", "Smart Door Lock")
                }
            }

            Spacer(Modifier.height(10.dp))

            // View Camera button
            Button(
                onClick = onViewCamera,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Lihat Kamera",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontSize = 13.sp,
            color = Color(0xFF6B6B6B),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color(0xFF1A1A1A),
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDetailTime(timestamp: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp)
        val outputFormatter = SimpleDateFormat("dd MMM yyyy, HH.mm.ss", Locale("id", "ID"))
        outputFormatter.format(date ?: Date())
    } catch (e: Exception) {
        "Waktu tidak diketahui"
    }
}

@Preview(showBackground = true)
@Composable
private fun AccessHistoryDetailModalPreview() {
    val sampleAccessLog = com.authentic.smartdoor.dashboard.domain.model.AccessLog(
        id = "1",
        userId = "user1",
        doorId = "door1",
        action = "buka",
        timestamp = "2025-01-18T10:30:00.000Z",
        success = true,
        method = "face_recognition",
        ipAddress = "192.168.1.100",
        cameraCaptureId = "capture_123"
    )

    AccessHistoryDetailModal(
        accessLog = sampleAccessLog,
        onDismiss = {},
        onViewCamera = {}
    )
}