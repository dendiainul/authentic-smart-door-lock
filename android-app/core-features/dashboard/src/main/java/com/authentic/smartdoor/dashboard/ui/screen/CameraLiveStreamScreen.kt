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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.LinearProgressIndicator
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

@Composable
fun CameraLiveStreamScreen(
    doorName: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var playbackPosition by remember { mutableStateOf(0f) }
    var isControlsVisible by remember { mutableStateOf(true) }

    // Simulate loading for 2 seconds
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        isLoading = false
        isPlaying = true
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Black,
        topBar = {
            if (isControlsVisible) {
                CameraTopBar(
                    doorName = doorName,
                    onBackClick = onBackClick,
                    isFullscreen = isFullscreen,
                    onFullscreenToggle = { isFullscreen = !isFullscreen }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Video Player Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (isLoading) {
                    // Loading State
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF6C63FF),
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Menghubungkan ke kamera...",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    // Video Player Placeholder (since we don't have actual stream URL)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated video content
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF1A1A1A))
                        ) {
                            // Simulated camera feed pattern
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📹",
                                    fontSize = 64.sp
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "Live Stream: $doorName",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Kamera aktif - Live",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Controls Overlay
                if (isControlsVisible && !isLoading) {
                    CameraControlsOverlay(
                        isPlaying = isPlaying,
                        isMuted = isMuted,
                        playbackPosition = playbackPosition,
                        onPlayPauseClick = { isPlaying = !isPlaying },
                        onMuteClick = { isMuted = !isMuted },
                        onPositionChange = { playbackPosition = it }
                    )
                }

                // Tap to show/hide controls
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    // Invisible tap area
                }
            }
        }
    }
}

@Composable
private fun CameraTopBar(
    doorName: String,
    onBackClick: () -> Unit,
    isFullscreen: Boolean,
    onFullscreenToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Column {
                    Text(
                        text = doorName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Live Stream",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onFullscreenToggle,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CameraControlsOverlay(
    isPlaying: Boolean,
    isMuted: Boolean,
    playbackPosition: Float,
    onPlayPauseClick: () -> Unit,
    onMuteClick: () -> Unit,
    onPositionChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Bottom Controls
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Progress Bar
                LinearProgressIndicator(
                    progress = playbackPosition,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFF6C63FF),
                    trackColor = Color.White.copy(alpha = 0.3f)
                )

                // Control Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play/Pause Button
                    FloatingActionButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier.size(56.dp),
                        containerColor = Color(0xFF6C63FF),
                        contentColor = Color.White
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Mute Button
                    IconButton(
                        onClick = onMuteClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraLiveStreamScreenPreview() {
    CameraLiveStreamScreen(
        doorName = "Pintu Depan",
        onBackClick = {}
    )
}
