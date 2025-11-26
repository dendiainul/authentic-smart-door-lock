package com.authentic.smartdoor.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import com.authentic.smartdoor.authentication.ui.theme.AuthenticationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.authentic.smartdoor.authentication.presentation.auth.AuthNavigationEvent
import com.authentic.smartdoor.authentication.presentation.auth.AuthViewModel
import com.authentic.smartdoor.authentication.ui.theme.PurplePrimary
import com.authentic.smartdoor.authentication.ui.theme.abhayaLibre
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthenticationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, viewModel: AuthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        android.util.Log.d("AuthenticationActivity", "Google Sign-In result received: ${result.resultCode}")
        viewModel.handleGoogleSignInResult(result.data)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                AuthNavigationEvent.NavigateToHome -> {
                    val activity = context as? Activity
                    val user = uiState.user
                    val resultIntent = Intent().apply {
                        putExtra("user_id", user?.id ?: "")
                        putExtra("user_email", user?.email ?: "")
                        putExtra("user_name", user?.name ?: "")
                    }
                    activity?.setResult(Activity.RESULT_OK, resultIntent)
                    activity?.finish()
                }
                AuthNavigationEvent.NavigateToLogin -> {
                    // No-op for now
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo SecureDoor
            Image(
                painter = painterResource(id = R.drawable.securedoor),
                contentDescription = "SecureDoor Logo",
                modifier = Modifier
                    .size(width = 250.dp, height = 80.dp),
                contentScale = ContentScale.Fit
            )

            // Gambar ilustrasi - diperbesar lagi
            Image(
                painter = painterResource(id = R.drawable.auth_image),
                contentDescription = "Auth Illustration",
                modifier = Modifier
                    .size(width = 380.dp, height = 240.dp), // Diperbesar dari 320x180
                contentScale = ContentScale.Fit
            )

            // Box untuk menyelaraskan lebar teks dan tombol
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Teks utama
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.Black)) {
                                append("Selamat Datang\n")
                            }
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.Black)) {
                                append("di ")
                            }
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = PurplePrimary)) {
                                append("SecureDoor!!")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = abhayaLibre,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        textAlign = TextAlign.Center
                    )

                    // Subteks - menggunakan modifier yang sama untuk lebar
                    Text(
                        text = "Masuk dengan akun Google untuk mengakses sistem pintu cerdas Anda",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp) // Lebar referensi
                    )

                    // Tombol Google - dengan drop shadow dan lebar yang sama
                    Button(
                        onClick = {
                            android.util.Log.d("AuthenticationActivity", "=== Google Sign-In button clicked ===")
                            val intent = viewModel.getGoogleSignInIntent()
                            android.util.Log.d("AuthenticationActivity", "Google Sign-In intent: $intent")
                            launcher.launch(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        ),
                        modifier = Modifier
                            .padding(horizontal = 32.dp) // Sama dengan padding subteks
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "Google Icon",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Continue with Google",
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}




@Preview(showBackground = true, name = "Login Splash")
@Composable
fun LoginScreenPreview() {
    AuthenticationTheme {
        LoginScreen()
    }
}

