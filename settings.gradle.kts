pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://raw.githubusercontent.com/alexgreench/google-webrtc/master") }
    }
}

rootProject.name = "PrivacyCall"
include(":androidApp")
include(":shared")
