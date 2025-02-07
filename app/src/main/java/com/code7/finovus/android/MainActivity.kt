package com.code7.finovus.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.common.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure Firebase is initialized before using FirebaseAuth
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
            Log.d("Firebase", "Firebase initialized in MainActivity")
        }

        // Initialize FirebaseAuth after ensuring Firebase is initialized
        val firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In options
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("871117153280-9rud6ff3u650h9e71gpvphmu90j8udql.apps.googleusercontent.com") // Replace with your actual client ID
            .requestEmail()
            .build()

        // Initialize GoogleSignInClient
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        setContent {
            AppNavigation(
                firebaseAuth = firebaseAuth,
                googleSignInClient = googleSignInClient // Pass the GoogleSignInClient
            )
        }
    }
}
