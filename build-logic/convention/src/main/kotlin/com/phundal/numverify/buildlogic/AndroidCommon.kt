package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.CommonExtension

/**
 * Configuration shared by every Android module, application or library.
 *
 * `targetSdk` is deliberately absent: it is a property of the shipped application, not of a
 * library, and AGP only exposes it on the application and test DSLs.
 */
internal fun CommonExtension.configureAndroidCommon() {
    compileSdk = BuildDefaults.COMPILE_SDK

    defaultConfig.minSdk = BuildDefaults.MIN_SDK

    compileOptions.sourceCompatibility = BuildDefaults.JAVA_VERSION
    compileOptions.targetCompatibility = BuildDefaults.JAVA_VERSION
}
