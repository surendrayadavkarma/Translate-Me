pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Allow for flexibility in repo selection
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TranslateMe"
include(":app")
