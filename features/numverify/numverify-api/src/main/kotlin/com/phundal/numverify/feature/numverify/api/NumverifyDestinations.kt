package com.phundal.numverify.feature.numverify.api

/**
 * The routes this feature answers to.
 *
 * Declared here, in the JVM-only contract, so that another feature can navigate to a NumVerify
 * screen by depending on this module alone — never on `numverify-impl` or `numverify-ui`.
 */
object NumverifyDestinations {

    /** The phone-number verification screen. */
    const val VERIFY_NUMBER: String = "numverify/verify"
}
