package com.authentic.smartdoor.authentication.utils

import android.content.Context
import com.authentic.smartdoor.authentication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getGoogleSignInClient(): GoogleSignInClient {
        return try {
            // Use the web client ID from strings.xml
            val clientId = try {
                val id = context.getString(R.string.default_web_client_id)
                android.util.Log.d("GoogleSignInHelper", "Client ID from strings.xml: $id")
                id
            } catch (e: Exception) {
                android.util.Log.e("GoogleSignInHelper", "Failed to get Client ID from strings.xml: ${e.message}")
                // Fallback client ID - replace with your actual web client ID
                "904749622966-5u5875kdik0vir2v8monrjja972f8ud0.apps.googleusercontent.com"
            }
            
            // Try using Android Client ID instead of Web Client ID
            val finalClientId = "904749622966-k9m2mf682a93ej1e991m2nkj5aal8k65.apps.googleusercontent.com"
            android.util.Log.d("GoogleSignInHelper", "Using Android Client ID: $finalClientId")

            android.util.Log.d("GoogleSignInHelper", "Using Client ID: $finalClientId")

            if (finalClientId.isEmpty() || finalClientId.contains("YOUR_CLIENT_ID")) {
                throw IllegalStateException("Google Client ID not properly configured. Please add valid client ID to strings.xml")
            }

            // Try without requestIdToken first
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
            
            android.util.Log.d("GoogleSignInHelper", "GoogleSignInOptions without requestIdToken")

            android.util.Log.d("GoogleSignInHelper", "GoogleSignInOptions created successfully")
            GoogleSignIn.getClient(context, gso)
        } catch (e: Exception) {
            android.util.Log.e("GoogleSignInHelper", "Failed to initialize Google Sign-In: ${e.message}", e)
            throw IllegalStateException("Failed to initialize Google Sign-In: ${e.message}", e)
        }
    }
    }
