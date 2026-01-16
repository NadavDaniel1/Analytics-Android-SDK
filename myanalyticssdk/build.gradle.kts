plugins {
    // שינוי קריטי: הגדרת הפרויקט כספרייה ולא כאפליקציה
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.myanalyticssdk"
    compileSdk = 34

    defaultConfig {
        // שים לב: מחקתי את applicationId - אסור שיהיה לספרייה!
        minSdk = 26

        // גרסאות המניפסט של הספרייה (אופציונלי אך מומלץ)
        aarMetadata {
            minCompileSdk = 26
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // מחקתי את buildFeatures { compose = true } - ה-SDK לא צריך מסכים
}

dependencies {
    // === רכיבי בסיס ===
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // === Retrofit (השליח - לתקשורת רשת) ===
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // === Room (היומן - מסד נתונים מקומי) ===
    val roomVersion = "2.6.1"
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // טסטים (לא חובה כרגע, אבל שיהיה)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}