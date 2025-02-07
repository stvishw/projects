package com.code7.finovus.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    // Determine the next activity based on authentication state
    val nextActivity = if (firebaseAuth.currentUser != null) {
        MainActivity::class.java // User is logged in; navigate to MainActivity
    } else {
        MainActivity::class.java // User is not logged in; navigate to LoginScreen
    }

    // Animation state
    var morphToLogo by remember { mutableStateOf(false) }

    // Trigger morphing after delay
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            morphToLogo = true
        }, 800) // Morphing delay

        // Navigate to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(context, nextActivity)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish()
        }, 2000)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bc1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(), // Fills the entire available screen space
            contentScale = ContentScale.FillBounds // Ensures the image fills both width and height
        )

        // Overlay content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Ensure content inside is centered
        ) {
            if (!morphToLogo) {
                MorphingProgressBar()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LogoAnimation()

                    Spacer(modifier = Modifier.height(16.dp)) // Add some spacing between logo and text

                    // Text "Finovus"
                    Text(
                        text = "Finovus For Your Expenses",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSecondary),
                        fontSize = 23.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MorphingProgressBar() {
    // Animate size and stroke width
    val size = remember { Animatable(100f) }
    val strokeWidth = remember { Animatable(8f) }
    val color = remember { Animatable(Color.Red) }

    LaunchedEffect(Unit) {
        // Animate size, stroke width, and color to create a morphing effect
        size.animateTo(
            targetValue = 150f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )
        strokeWidth.animateTo(
            targetValue = 2f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )
    }

    CircularProgressIndicator(
        modifier = Modifier
            .size(size.value.dp),
        color = color.value,
        strokeWidth = strokeWidth.value.dp
    )
}


@Composable
fun LogoAnimation() {
    Surface(
        modifier = Modifier
            .size(150.dp),
            //.clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplashScreen()
}
