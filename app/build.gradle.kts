plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.skeeper.minicode"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.skeeper.minicode"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.11.0")

    // ui
    implementation("com.google.android.flexbox:flexbox:3.0.0")


    // security
    implementation("at.favre.lib:bcrypt:0.9.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // di
    implementation("com.google.dagger:hilt-android:2.48")
    annotationProcessor("com.google.dagger:hilt-android-compiler:2.48")

    // app
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.github.amrdeveloper:codeview:1.3.9")

    // net
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")



    // JGIT bottom
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.12.0.202106070339-r")
    // SSH
//    implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.apache:6.4.0.202211300538-r")
//    // GPG
//    implementation("org.eclipse.jgit:org.eclipse.jgit.gpg.bc:6.4.0.202211300538-r")


}