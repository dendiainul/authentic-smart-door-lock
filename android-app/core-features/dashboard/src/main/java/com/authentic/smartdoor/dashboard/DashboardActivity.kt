package com.authentic.smartdoor.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.authentic.smartdoor.dashboard.ui.DashboardNavigation
import com.authentic.smartdoor.dashboard.ui.theme.DashboardTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.content.SharedPreferences
import android.widget.Toast
import com.authentic.smartdoor.storage.preferences.PreferencesManager

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    @Inject lateinit var preferencesManager: PreferencesManager
    private var prefListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
    private var authLaunching: Boolean = false
    private val authLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        authLaunching = false
        if (result.resultCode != RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!preferencesManager.isLoggedIn()) {
            Toast.makeText(this, "Sesi telah habis", Toast.LENGTH_SHORT).show()
            if (!authLaunching) {
                authLaunching = true
                authLauncher.launch(Intent(this, com.authentic.smartdoor.authentication.AuthenticationActivity::class.java))
            }
        }
        val prefs = getSharedPreferences("smart_door_prefs", Context.MODE_PRIVATE)
        prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "is_logged_in" && !preferencesManager.isLoggedIn()) {
                if (!authLaunching) {
                    authLaunching = true
                    Toast.makeText(this, "Sesi telah habis", Toast.LENGTH_SHORT).show()
                    authLauncher.launch(Intent(this, com.authentic.smartdoor.authentication.AuthenticationActivity::class.java))
                }
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
        enableEdgeToEdge()
        setContent {
            DashboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = getSharedPreferences("smart_door_prefs", Context.MODE_PRIVATE)
        prefListener?.let { prefs.unregisterOnSharedPreferenceChangeListener(it) }
        prefListener = null
    }
}
