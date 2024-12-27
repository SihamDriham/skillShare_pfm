plugins {
    alias(libs.plugins.android.application)
    id("org.sonarqube") version "6.0.1.5171"
}


android {
    namespace = "ma.ensaj.skillshare_front"
    compileSdk = 34

    viewBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "ma.ensaj.skillshare_front"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled=true

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "skillshare_front")
        property("sonar.projectName", "skillshare_front")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", "sqp_b49c5bccf55458d5059035e3d009141c70bf3e8b")
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

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.jwtdecode)
    
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.navigation.fragment.v276)
    implementation(libs.navigation.ui.v276)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    implementation(libs.hdodenhof.circleimageview)
    implementation(libs.material.v190)

}