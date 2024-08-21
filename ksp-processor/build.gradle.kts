plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.google.devtools.ksp.symbol.processing.api)
    implementation(libs.squareup.kotlinpoet.ksp)
}