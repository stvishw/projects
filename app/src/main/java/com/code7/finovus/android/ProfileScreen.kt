package com.code7.finovus.android

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.code7.finovus.android.ui.theme.Green
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient
) {
    val currentUser = firebaseAuth.currentUser
    val userName = currentUser?.displayName ?: "John Doe"
    val userEmail = currentUser?.email ?: "john.doe@example.com"
    val lastLoginDate = currentUser?.metadata?.lastSignInTimestamp?.let {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(it))
    } ?: "Not Set"
    val userPhotoUrl = currentUser?.photoUrl ?: ""

    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Lighter background for modern look
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Section
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(userPhotoUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp) // Increased size for better visibility
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f)) // Subtle background
                        .border(2.dp, Color.Gray, CircleShape) // Border for distinction
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            ProfileInputField(label = "First Name", value = userName.split(" ").firstOrNull() ?: "John", isEditable = false)
            Spacer(modifier = Modifier.height(8.dp))

            ProfileInputField(label = "Last Name", value = userName.split(" ").lastOrNull() ?: "Doe", isEditable = false)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileInputField(label = "Email", value = userEmail, isEditable = false)
            ProfileInputField(label = "Last Login", value = lastLoginDate, isEditable = false)
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Button(
                onClick = {
                    navController.navigate("main") {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Slightly increased height for comfort
            ) {
                Text(text = "Continue to Home", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Logout", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = "Logout Confirmation")
            },
            text = {
                Text("Are you sure you want to logout?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        logOut(firebaseAuth, googleSignInClient) { navController.navigate("login") }
                        showLogoutDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputField(label: String, value: String, isEditable: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground // Adjust text color for better contrast
            )
        )
        if (isEditable) {
            TextField(
                value = value,
                onValueChange = { /* Handle changes here */ },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFE0E0E0), // Lighter background for better visibility
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground // Adjust text color for better contrast
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }
}

// Log Out Function
fun logOut(firebaseAuth: FirebaseAuth, googleSignInClient: GoogleSignInClient, onLogout: () -> Unit) {
    firebaseAuth.signOut()
    googleSignInClient.signOut().addOnCompleteListener {
        if (it.isSuccessful) {
            onLogout()
        } else {
            Log.e("LogOut", "Failed to sign out: ${it.exception?.message}")
        }
    }
}
