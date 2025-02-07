package com.code7.finovus.android;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
//import androidx.activity.compose.setContent;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.code7.expensetracker.android.LoginScreen; // Ensure LoginScreen is imported

public class LoginActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge
        EdgeToEdge.enable(this);

        // Set the content view to a composable screen
        Object setContent = null;
        Object setContent1 = setContent;
        {
            // LoginScreen is your composable for login UI
            LoginScreen();
        }

        // Handle window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void LoginScreen() {
    }

}
