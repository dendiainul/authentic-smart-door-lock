    package com.authentic.smartdoor

    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.wrapContentWidth
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.sp
    import androidx.compose.material3.ElevatedButton
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.ArrowForward
    import androidx.compose.animation.core.tween
    import androidx.compose.animation.slideInHorizontally
    import androidx.compose.animation.slideOutHorizontally
    import androidx.compose.animation.AnimatedContent
    import androidx.compose.animation.ExperimentalAnimationApi
    import androidx.compose.animation.fadeIn
    import androidx.compose.animation.fadeOut
    import androidx.compose.animation.with
    import androidx.compose.ui.platform.LocalContext
    import com.authentic.smartdoor.ui.theme.SecureDoorTheme
    import com.authentic.smartdoor.ui.theme.PurplePrimary
    import com.authentic.smartdoor.ui.theme.abhayaLibre
    import com.authentic.smartdoor.ui.theme.jura
    import kotlinx.coroutines.delay
    import kotlin.jvm.java

    class MainActivity : ComponentActivity() {
        
        private val authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Authentication successful, navigate to dashboard
                val intent = Intent(this, com.authentic.smartdoor.dashboard.DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                SecureDoorTheme {
                    var showSplash by remember { mutableStateOf(true) }
                    var showSmartUnlock by remember { mutableStateOf(false) }
                    var showRealTimeAccess by remember { mutableStateOf(false) }
                    var showHistoryAlert by remember { mutableStateOf(false)}

                    LaunchedEffect(Unit) {
                        delay(1800)
                        showSplash = false
                    }

                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        when {
                            showSplash -> {
                                SplashScreen(modifier = Modifier.padding(innerPadding))
                            }
                            showHistoryAlert -> {
                                HistoryAlertScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onBackClick = {
                                        showHistoryAlert = false
                                        showRealTimeAccess = true
                                    },
                                    onNextClick = { context ->
                                        val intent = Intent(
                                            context,
                                            com.authentic.smartdoor.authentication.AuthenticationActivity::class.java
                                        )
                                        authLauncher.launch(intent)
                                    }
                                )
                            }
                            showRealTimeAccess -> {
                                RealTimeAccessScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onBackClick = {
                                        showRealTimeAccess = false
                                        showSmartUnlock = true
                                    },
                                    onNextClick = {
                                        showRealTimeAccess = false
                                        showHistoryAlert = true
                                    }
                                )
                            }
                            showSmartUnlock -> {
                                SmartUnlockScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onBackClick = {
                                        showSmartUnlock = false
                                    },
                                    onNextClick = {
                                        showSmartUnlock = false
                                        showRealTimeAccess = true
                                    }
                                )
                            }
                            else -> {
                                OnboardingScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onGetStartedClick = {
                                        showSmartUnlock = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SplashScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.securedoor),
                contentDescription = "SecureDoor logo"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Sederhanakan Akses, Tingkatkan Keamanan",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun OnboardingScreen(
        modifier: Modifier = Modifier,
        onGetStartedClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_image_1),
                    contentDescription = "Onboarding illustration",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .offset(y = (-2).dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(PurplePrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.height(8.dp))

                        // Container untuk semua elemen dengan lebar yang sama
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "SECUREDOOR",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontFamily = abhayaLibre, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "SecureDoor mengubah pintu biasa menjadi \ntempat rasa aman dimulai.",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = abhayaLibre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                                ElevatedButton(
                                    onClick = onGetStartedClick,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = Color(0xFFC4BFFF),
                                        contentColor = Color.Black
                                    ),
                                    elevation = ButtonDefaults.elevatedButtonElevation(
                                        defaultElevation = 8.dp,
                                        pressedElevation = 12.dp,
                                        hoveredElevation = 10.dp
                                    )
                                ) {
                                    Text(
                                        text = "Get Started",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = abhayaLibre,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Floating SECUREDOOR logo overlay
            Image(
                painter = painterResource(id = R.drawable.securedoor),
                contentDescription = "SECUREDOOR logo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-80).dp)
                    .size(310.dp)
            )
        }
    }

    @Composable
    fun SmartUnlockScreen(
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit = {},
        onNextClick: () -> Unit = {}
    ) {
        Box(modifier = modifier.fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Lock icon with purple gradient background
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_image_2),
                        contentDescription = "Smart Lock",
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = "Smart Unlock",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Buka pintu rumah atau kantor dengan\nsmart hanya melalui ponsel Anda.\nTanpa kunci fisik, lebih praktis dan modern.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // Navigation buttons at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PurplePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Next button
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun RealTimeAccessScreen(
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit = {},
        onNextClick: () -> Unit = {}
    ) {
        Box(modifier = modifier.fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Real-time access icon with purple gradient background
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_image_3),
                        contentDescription = "Real-Time Access",
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = "Real-Time Access",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Kontrol pintu secara  langsung kapan saja. \n" +
                            "Lihat status terkini dan kelola akses dalam hitungan detik.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // Navigation buttons at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PurplePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Next button
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun HistoryAlertScreen(
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit = {},
        onNextClick: (Context) -> Unit = {}
    ) {
        val context = LocalContext.current
        Box(modifier = modifier.fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Real-time access icon with purple gradient background
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_image_4),
                        contentDescription = "History & Alert",
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = "History & Alert",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Pantau riwayat akses pintu dan terima notifikasi segera \n" +
                            "untuk setiap aktivitas, sukses maupun percobaan gagal.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = jura,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // Navigation buttons at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PurplePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Next button
                IconButton(
                    onClick = { onNextClick(context) },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PurplePrimary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, name = "Splash")
    @Composable
    fun SplashPreview() {
        SecureDoorTheme {
            SplashScreen()
        }
    }

    @Preview(showBackground = true, name = "Onboarding")
    @Composable
    fun OnboardingPreview() {
        SecureDoorTheme {
            OnboardingScreen()
        }
    }

    @Preview(showBackground = true, name = "Smart Unlock")
    @Composable
    fun SmartUnlockPreview() {
        SecureDoorTheme {
            SmartUnlockScreen()
        }
    }

    @Preview(showBackground = true, name = "Real-Time Access")
    @Composable
    fun RealTimeAccessPreview() {
        SecureDoorTheme {
            RealTimeAccessScreen()
        }
    }

    @Preview(showBackground = true, name = "History & Alert")
    @Composable
    fun HistoryAlertPreview() {
        SecureDoorTheme {
            HistoryAlertScreen()
        }
    }