package com.example.analyticsproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.analyticsproject.ui.theme.AnalyticsProjectTheme
import com.example.myanalyticssdk.AnalyticsManager // Import your custom SDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ==========================================
        // 1. Initialize the Analytics SDK
        // ==========================================
        // This sets up the database and network configurations using the application context.
        // It must be called once when the app starts.
        AnalyticsManager.init(this)

        // ==========================================
        // 2. Standard UI Setup (Jetpack Compose)
        // ==========================================
        enableEdgeToEdge()
        setContent {
            AnalyticsProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // We call our new screen that contains the testing buttons
                    AnalyticsTestScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * A simple screen with buttons to manually trigger analytics events.
 * This prevents events from firing automatically on every app restart or rotation.
 */
@Composable
fun AnalyticsTestScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Analytics SDK Tester")

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // Test A: Simple Button Click
        // ==========================================
        Button(onClick = {
            // Track a basic interaction event with user properties
            AnalyticsManager.trackEvent("Button_Clicked", mapOf("user_type" to "student"))
        }) {
            Text("Send Click Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // Test B: Complex Event (Purchase)
        // ==========================================
        Button(onClick = {
            // Track a business event with numeric properties (price)
            AnalyticsManager.trackEvent("Purchase_Made", mapOf(
                "item" to "Gold Subscription",
                "price" to 99.90,
                "currency" to "USD"
            ))
        }) {
            Text("Send Purchase Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // Test C: Device Info
        // ==========================================
        Button(onClick = {
            // Track system details dynamically
            AnalyticsManager.trackEvent("Device_Info", mapOf(
                "model" to android.os.Build.MODEL,
                "os_version" to android.os.Build.VERSION.SDK_INT
            ))
        }) {
            Text("Send Device Info")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnalyticsProjectTheme {
        AnalyticsTestScreen()
    }
}