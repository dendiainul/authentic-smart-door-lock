package com.authentic.smartdoor.dashboard.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.authentic.smartdoor.dashboard.R

@Composable
fun DoorConfirmationModal(
    isVisible: Boolean,
    doorName: String,
    action: String, // "buka" atau "kunci"
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false // Tambahkan ini
            )
        ) {
            // Bottom sheet style modal
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Background overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )

                // Modal content
                Card(
                    modifier = Modifier
                        .fillMaxWidth(), // Hapus padding horizontal
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Lock image
                        Image(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(Modifier.height(24.dp))

                        // Title
                        Text(
                            text = "Konfirmasi",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF1A1A1A)
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        // Message
                        Text(
                            text = "Apakah Anda yakin ingin ${action} $doorName ?",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                color = Color(0xFF1A1A1A)
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(32.dp))

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Cancel button
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF5F5F5),
                                    contentColor = Color(0xFF1A1A1A)
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(
                                    text = "Batal",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Confirm button
                            Button(
                                onClick = onConfirm,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50),
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(
                                    text = "Ya, ${action.replaceFirstChar { it.uppercase() }}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Add bottom padding for safe area
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}