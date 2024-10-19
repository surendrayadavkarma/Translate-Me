plugins {
    id("com.android.application") version "8.6.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false // Ensure correct version
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // Ensure correct version
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
