plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.dagger.hilt) apply false
    // Update the Google services plugin version
    id("com.google.gms.google-services") version "4.3.15" apply false
}
