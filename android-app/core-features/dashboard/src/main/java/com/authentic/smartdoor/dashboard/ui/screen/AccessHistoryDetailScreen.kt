package com.authentic.smartdoor.dashboard.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import com.authentic.smartdoor.dashboard.domain.model.User
import com.authentic.smartdoor.dashboard.domain.model.Door
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessHistoryDetailScreen(
    accessLog: AccessLog,
    onBackClick: () -> Unit = {},
    onViewCamera: () -> Unit = {},
    // Add refresh function
    onRefresh: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    
    var isRefreshing by remember { mutableStateOf(false) }
    
    // SwipeRefresh state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

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

    Scaffold(
        containerColor = Color(0xFFF7F6FF),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Riwayat Akses",
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
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { 
                onRefresh()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Status Card
            StatusCard(
                icon = icon,
                iconColor = iconColor,
                statusText = statusText,
                accessLog = accessLog
            )


            // Location Information Card
            LocationInfoCard(accessLog = accessLog)

            // User Information Card
            UserInfoCard(accessLog = accessLog)

            // Access Details Card
            AccessDetailsCard(accessLog = accessLog)

            // Camera Button
            Button(
                onClick = onViewCamera,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Lihat Rekaman Kamera",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun StatusCard(
    icon: ImageVector,
    iconColor: Color,
    statusText: String,
    accessLog: AccessLog
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = statusText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = accessLog.door?.name ?: "Pintu ${accessLog.doorId}",
                fontSize = 16.sp,
                color = Color(0xFF6B6B6B)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = formatDetailTime(accessLog.timestamp),
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}


@Composable
private fun LocationInfoCard(accessLog: AccessLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF9C27B0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Informasi Lokasi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(Modifier.height(16.dp))

            DetailRow("Pintu", accessLog.door?.name ?: "Pintu ${accessLog.doorId}")
            DetailRow("Gedung", accessLog.door?.location ?: "Lokasi tidak diketahui")
        }
    }
}

@Composable
private fun UserInfoCard(accessLog: AccessLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Informasi Pengguna",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(Modifier.height(16.dp))

            DetailRow("Nama", accessLog.user?.name ?: "User ${accessLog.userId}")
            DetailRow("Email", accessLog.user?.email ?: "Email tidak diketahui")
            DetailRow("Status", if (accessLog.success) "Berhasil" else "Ditolak")
        }
    }
}

@Composable
private fun AccessDetailsCard(accessLog: AccessLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Detail Akses",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(Modifier.height(16.dp))

            DetailRow("Metode", getMethodDisplayName(accessLog.method))
            DetailRow("Status", if (accessLog.success) "Berhasil" else "Ditolak")
            DetailRow("Tindakan", if (accessLog.action == "buka") "Membuka Pintu" else "Mengunci Pintu")
            DetailRow("IP Address", accessLog.ipAddress)
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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF6B6B6B),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF1A1A1A),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}




private fun getMethodDisplayName(method: String): String {
    return when (method) {
        "face_recognition" -> "Pengenalan Wajah"
        "mobile_app" -> "Aplikasi Mobile"
        "card" -> "Kartu Akses"
        else -> method
    }
}





private fun formatDetailTime(timestamp: String): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = formatter.parse(timestamp)
        val outputFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale("id", "ID"))
        outputFormatter.format(date ?: Date())
    } catch (e: Exception) {
        "Waktu tidak diketahui"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F6FF)
@Composable
private fun AccessHistoryDetailScreenPreview() {
    val sampleAccessLog = AccessLog(
        id = "108",
        userId = "1",
        doorId = "6",
        action = "buka",
        timestamp = "2025-10-23T07:44:06.948Z",
        success = true,
        method = "mobile_app",
        ipAddress = "192.168.1.52",
        cameraCaptureId = null,
        user = User(
            id = "1",
            name = "Hafiz Atama Romadhoni",
            email = "hafizganzzxd@gmail.com",
            avatar = "https://randomuser.me/api/portraits/women/13.jpg",
            role = "admin",
            faceRegistered = true
        ),
        door = Door(
            id = "6",
            name = "Pintu Ruang Administrasi",
            location = "Gedung Kantin Area Tengah Kampus",
            locked = true,
            batteryLevel = 75,
            lastUpdate = "2025-10-23T06:23:40.094Z",
            wifiStrength = "Weak",
            cameraActive = true
        ),
        cameraCapture = null
    )

    AccessHistoryDetailScreen(
        accessLog = sampleAccessLog,
        onBackClick = {},
        onViewCamera = {},
        onRefresh = {}
    )
}
