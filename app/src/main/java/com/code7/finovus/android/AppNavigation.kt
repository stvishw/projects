package com.code7.finovus.android

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun AppNavigation(
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient // Include GoogleSignInClient
) {
    val navController = rememberNavController()

    // Check if the user is logged in or not
    val startDestination = if (firebaseAuth.currentUser != null) {
        "main" // If the user is logged in, navigate to main screen
        //"login"

    } else {
        "login" // If the user is not logged in, navigate to login screen
        //"main"
    }

    // Define the navigation graph
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            // Pass both firebaseAuth and googleSignInClient to LoginScreen
            LoginScreen(navController, firebaseAuth, googleSignInClient)
        }
        composable("main") {
            NavHostScreen() // Your existing main screen
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                firebaseAuth = firebaseAuth, // Pass firebaseAuth here
                googleSignInClient = googleSignInClient // Pass GoogleSignInClient here
            )
        }
    }
}
