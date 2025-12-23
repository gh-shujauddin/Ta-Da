// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    val kotlinVersion = "2.2.21"

    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.6" apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "2.2.21-2.0.4" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.57.2" apply false

}