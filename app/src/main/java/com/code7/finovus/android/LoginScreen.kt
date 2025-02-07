package com.code7.finovus.android

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image

// Define RC_SIGN_IN constant
private const val RC_SIGN_IN = 9001


@Composable
fun LoginScreen(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient
) {
    var errorMessage by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }
    val activity = navController.context as Activity

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                isSigningIn = true
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken!!, firebaseAuth, {
                        isSigningIn = false
                        navController.navigate("profile")
                    }, {
                        isSigningIn = false
                        errorMessage = it
                    })
                } catch (e: ApiException) {
                    isSigningIn = false
                    Log.w("Google Sign-In", "Google sign in failed", e)
                    errorMessage = "Google Sign-In failed"
                }
            } else {
                isSigningIn = false
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Set your background image from a URL
//        AsyncImage(
//            model = "https://drive.google.com/file/d/1i0lsNltiLEyN-GjiGjEKedvSmVsob1Wb/view?usp=drive_link", // Replace with your direct image URL
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
        Image(
            painter = painterResource(id = R.drawable.bc1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(), // Fills the entire available screen space
            contentScale = ContentScale.FillBounds // Ensures the image fills both width and height
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Introduction Text with Writing Effect
            WritingText()

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign-In Button
            Button(
                onClick = {
                    isSigningIn = true
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSigningIn, // Button is disabled during sign-in
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Background color when enabled
                    contentColor = Color.Black, // Text color when enabled
                    disabledContainerColor = Color.White, // Background color when disabled
                    disabledContentColor = Color.Gray // Text color when disabled
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.g2),
                        contentDescription = "Google Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Sign In with Google",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }


            if (isSigningIn) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = Color.Red // Sets the progress indicator color to red

                )
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.background),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        val privacyUrl = "https://sites.google.com/view/privacypolicy-finovus/home"
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "|")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Terms & Conditions",
                    style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.background),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        val termsUrl = "https://sites.google.com/view/terms-condition-finovus/home"
                        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl)))
                    }
                )
            }
        }

        Text(
            text = "Version 1.4 - Made with ❤\uFE0F in India",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(28.dp)
        )
    }
}

@Composable
fun WritingText() {
    var displayedText by remember { mutableStateOf("") }
    val fullText = "Manage your budget seamlessly"

    LaunchedEffect(Unit) {
        fullText.forEachIndexed { index, _ ->
            displayedText = fullText.substring(0, index + 1)
            kotlinx.coroutines.delay(100)
        }
    }

    Text(
        text = displayedText,
        style = TextStyle(
            fontSize = 24.sp,
            color = Color.White,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.5f),
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        ),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}


// Firebase authentication with Google credentials
private fun firebaseAuthWithGoogle(
    idToken: String,
    firebaseAuth: FirebaseAuth,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                Log.w("FirebaseAuth", "signInWithCredential:failure", task.exception)
                onFailure("Authentication failed: ${task.exception?.message}")
            }
        }
}
