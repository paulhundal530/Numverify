package com.phundal.numverify.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/** The single source of truth for what every module in this build compiles against. */
internal object BuildDefaults {
    const val COMPILE_SDK = 37
    const val MIN_SDK = 24
    const val TARGET_SDK = 36

    /** Java/Kotlin bytecode level. Kept in step so AGP does not reject a mixed-target module. */
    val JAVA_VERSION: JavaVersion = JavaVersion.VERSION_11
    val JVM_TARGET: JvmTarget = JvmTarget.JVM_11
}
