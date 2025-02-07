package com.code7.finovus.android

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import android.util.Log

@HiltAndroidApp
class ExpenseTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase only if it's not already initialized
        if (FirebaseApp.getApps(this).isEmpty()) {
            try {
                FirebaseApp.initializeApp(this)
                Log.d("Firebase", "Firebase initialized successfully")
            } catch (e: Exception) {
                Log.e("Firebase", "Error initializing Firebase: ${e.message}", e)
            }
        } else {
            Log.d("Firebase", "Firebase already initialized")
        }
    }
}
