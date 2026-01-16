package com.example.analyticsproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.analyticsproject.ui.theme.AnalyticsProjectTheme
import com.example.myanalyticssdk.AnalyticsManager // Import your custom SDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ==========================================
        // 1. Initialize the Analytics SDK
        // ==========================================
        // This sets up the database and network configurations using the application context.
        AnalyticsManager.init(this)

        // ==========================================
        // 2. Track Sample Events
        // ==========================================

        // A. Simple test event to verify database storage
        AnalyticsManager.trackEvent("Button_Clicked", mapOf("user_type" to "student"))

        // B. Example of tracking screen navigation flow
        AnalyticsManager.trackEvent("Screen_Navigation", mapOf(
            "from_screen" to "Login",
            "to_screen" to "Home"
        ))

        // C. Example of tracking session duration (e.g., user stayed for 45 seconds)
        AnalyticsManager.trackEvent("Session_Ended", mapOf(
            "duration_seconds" to 45
        ))

        // D. Example of tracking device information dynamically
        AnalyticsManager.trackEvent("Device_Info", mapOf(
            "model" to android.os.Build.MODEL, // Automatically retrieves the device model
            "os_version" to android.os.Build.VERSION.SDK_INT // Retrieves the Android version
        ))

        // ==========================================
        // 3. Standard UI Setup (Jetpack Compose)
        // ==========================================
        enableEdgeToEdge()
        setContent {
            AnalyticsProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Analytics User",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnalyticsProjectTheme {
        Greeting("Android")
    }
}