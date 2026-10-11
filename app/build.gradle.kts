import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val signingFile = rootProject.file("keystore.properties")
val signingProps = Properties()

if (signingFile.isFile) {
    signingFile.inputStream().use {
        signingProps.load(it)
    }

    listOf(
        "storeFile",
        "storePassword",
        "keyAlias",
        "keyPassword"
    ).forEach { property ->
        check(!signingProps.getProperty(property).isNullOrBlank()) {
            "Propriedade de assinatura ausente: $property"
        }
    }
}

android {
    namespace = "com.example.mybasic.helloworld"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mybasic.helloworld"
        minSdk = 21
        targetSdk = 34
        versionCode = 2
        versionName = "0.2.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (signingFile.isFile) {
            create("release") {
                storeFile = rootProject.file(
                    signingProps.getProperty("storeFile")
                )
                storePassword =
                    signingProps.getProperty("storePassword")
                keyAlias =
                    signingProps.getProperty("keyAlias")
                keyPassword =
                    signingProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("release") {
            if (signingFile.isFile) {
                signingConfig =
                    signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("17")
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}