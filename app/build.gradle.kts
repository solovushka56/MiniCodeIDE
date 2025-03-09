plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.skeeper.minicode"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.skeeper.minicode"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
//    implementation("com.google.android.material:material:1.9.0") // для кнопки



    implementation("io.github.amrdeveloper:codeview:1.3.9")


    // JGIT bottom
    // https://central.sonatype.dev/artifact/org.eclipse.jgit/org.eclipse.jgit/
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
    // SSH support for JGit based on Apache MINA sshd
//    implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.apache:6.4.0.202211300538-r")
//    // GPG support for JGit based on BouncyCastle (commit signing)
//    implementation("org.eclipse.jgit:org.eclipse.jgit.gpg.bc:6.4.0.202211300538-r")


}