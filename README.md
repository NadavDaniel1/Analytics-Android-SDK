# Analytics Android SDK 📱

This repository contains the Android client library and a demo application for the Analytics Project.

## 📂 Project Structure
* **`myanalyticssdk`**: The core library module. It handles event tracking, local caching (Room), and syncing with the server (Retrofit).
* **`app`**: An example application that demonstrates how to integrate and use the SDK.

## 🚀 Getting Started

### 1. Installation
(Instructions will be added after JitPack setup)

### 2. Initialization
In your `Application` class or `MainActivity`:

```kotlin
AnalyticsManager.init(context)
3. Tracking Events
You can track any event with custom parameters:

Kotlin

// Simple event
AnalyticsManager.trackEvent("App_Opened", mapOf("user_id" to "123"))

// Complex event
AnalyticsManager.trackEvent("Level_Complete", mapOf(
    "level" to 5,
    "score" to 4500,
    "time_remaining" to 120
))
🛠 Features
Offline Support: Events are stored locally when there is no internet connection.

Automatic Sync: Events are dispatched to the server automatically when possible.

Generic Data: Supports flexible key-value pairs for event properties.

🔗 Backend
This SDK communicates with the Analytics Python Server.

[Link to Backend Repository](https://github.com/NadavDaniel1/Analytics-Server)
