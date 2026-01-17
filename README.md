# Analytics Android SDK 📱

This repository contains the Android client library and a demo application for the Analytics Project.
This SDK allows developers to track user events, handle offline scenarios, and sync data seamlessly with a remote backend.

## 🚀 Features

* **Easy Integration:** Simple setup with minimal configuration.
* **Offline Support:** Events are stored locally (using Room Database) when the device is offline.
* **Smart Batching:** Automatically syncs stored events when the network becomes available.
* **Efficient Networking:** Uses Retrofit for reliable API calls.
* **Cloud Backend:** Connected to a Python (Flask) server hosted on Render with MongoDB Atlas storage.


## Installation

### Step 1. Add the JitPack repository
Add this to your project-level `settings.gradle.kts` inside `dependencyResolutionManagement`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("[https://jitpack.io](https://jitpack.io)") } // <--- Add this line
    }
}
```

### Step 2. Add the dependency
Add the SDK to your module-level build.gradle.kts (usually app/build.gradle.kts):
```kotlin
dependencies {
    implementation("com.github.NadavDaniel1:Analytics-Android-SDK:1.0.1")
}
```

## Initialization
In your `Application` class or `MainActivity`:

```kotlin
AnalyticsManager.init(context)
```
## Tracking Events
You can track any event with custom parameters:

```kotlin
// Example: User clicked a button
AnalyticsManager.getInstance().logEvent("Button_Clicked")

// Example: User completed a purchase
AnalyticsManager.getInstance().logEvent("Purchase_Completed")

// Complex event
AnalyticsManager.trackEvent("Level_Complete", mapOf(
    "level" to 5,
    "score" to 4500,
    "time_remaining" to 120
))
```

## 🔗 Related Repositories
This SDK is designed to work with a custom backend. You can view the server-side code here:

[Link to Backend Repository](https://github.com/NadavDaniel1/Analytics-Server)


###🛡 Requirements
Android SDK 21+

Internet Permission in the host app (<uses-permission android:name="android.permission.INTERNET" />)