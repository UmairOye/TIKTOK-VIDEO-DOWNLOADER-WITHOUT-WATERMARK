import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.tiktokvideodownloaderwithoutwatermark"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tiktokvideodownloaderwithoutwatermark"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())
        val apiToken = properties.getProperty("API_TOKEN") ?: ""
        val baseUrl = properties.getProperty("BASE_URL") ?: ""
        val endPoints = properties.getProperty("END_POINTS") ?: ""

        buildConfigField(
            type = "String",
            name = "API_TOKEN",
            value = apiToken
        )

        buildConfigField(
            type = "String",
            name = "END_POINTS",
            value = endPoints
        )


        buildConfigField(
            type = "String",
            name = "BASE_URL",
            value = baseUrl
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    tasks.register<Wrapper>("wrapper") {
        gradleVersion = "7.2"
    }

    tasks.register("prepareKotlinBuildScriptModel"){}

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //SDP TO SHOW SAME VIEW GROUP SIZES ON ALL SCREENS
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //NAVIGATION TO NAVIGATION FROM ONE DESTINATION TO OTHER
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.1")

    //SSP FOR TO SET SAME TEXT SIZE ON ALL SCREEN SIZES
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

    //RETROFIT FOR NETWORK CALLS
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //GLIDE LIBRARY FOR IMAGE CACHING
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    //LOTTIE FILES TO SHOW ANIMATION
    implementation ("com.airbnb.android:lottie:6.2.0")

    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")



}