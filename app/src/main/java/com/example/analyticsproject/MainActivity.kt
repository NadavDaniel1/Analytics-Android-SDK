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
import com.example.myanalyticssdk.AnalyticsManager // ייבוא ה-SDK שלך

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ==========================================
        // 1. כאן אנחנו מפעילים את ה-SDK שבנית!
        // ==========================================
        AnalyticsManager.init(this)

        // 2. שולחים אירוע ניסיון כדי לראות שזה נשמר ב-DB
        AnalyticsManager.trackEvent("Button_Clicked", mapOf("user_type" to "student"))

        // 1. דוגמה לנתוני תנועה בין מסכים
        AnalyticsManager.trackEvent("Screen_Navigation", mapOf(
            "from_screen" to "Login",
            "to_screen" to "Home"
        ))

// 2. דוגמה למשך זמן שימוש (נניח המשתמש היה 45 שניות)
        AnalyticsManager.trackEvent("Session_Ended", mapOf(
            "duration_seconds" to 45
        ))

// 3. דוגמה לפרטי מכשיר
        AnalyticsManager.trackEvent("Device_Info", mapOf(
            "model" to android.os.Build.MODEL, // שולף אוטומטית את דגם המכשיר
            "os_version" to android.os.Build.VERSION.SDK_INT
        ))

        // מכאן זה הקוד הרגיל של התצוגה (לא נגענו בו)
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

