package com.code7.finovus.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {
    val username = mutableStateOf("")
    val password = mutableStateOf("")
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val logoutMessage = mutableStateOf("") // State for logout message

    // Simulate login validation
    fun isLoginValid(): Boolean {
        return username.value.isNotEmpty() && password.value.isNotEmpty()
    }

    // Simulate profile validation
    fun isProfileValid(): Boolean {
        return name.value.isNotEmpty() && email.value.contains("@")
    }

    // Logout logic
//    fun logout(onLogout: () -> Unit) {
//        // Sign out from FirebaseAuth (Google Auth)
//        FirebaseAuth.getInstance().signOut()
//
//        // Clear user state
//        username.value = ""
//        password.value = ""
//        name.value = ""
//        email.value = ""
//
//        // Set logout message
//        logoutMessage.value = "You have been successfully logged out."
//
//        // Notify UI about logout
//        onLogout()
//    }
    // Logout function remains unchanged
// Logout logic
    fun logout(onLogout: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
           // Log.d("FirebaseAuth", "Current user: ${currentUser.email}")
        } else {
          //  Log.d("FirebaseAuth", "No user is logged in.")
        }

        auth.signOut() // Attempt to sign out

        val loggedOutUser = auth.currentUser
        if (loggedOutUser == null) {
            //Log.d("FirebaseAuth", "User logged out successfully.")
        } else {
           // Log.d("FirebaseAuth", "Sign out failed.")
        }

        username.value = ""
        password.value = ""
        name.value = ""
        email.value = ""
        logoutMessage.value = "You have been successfully logged out."
        onLogout()
    }


}
