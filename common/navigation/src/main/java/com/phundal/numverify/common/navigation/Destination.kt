package com.phundal.numverify.common.navigation

/**
 * Identifies a screen that a feature can be asked to show.
 *
 * Routes are declared as plain constants by each feature's `api` module, which is why this wraps a
 * [String] rather than a sealed hierarchy: a feature can name another feature's destination by
 * depending only on its lightweight JVM contract, never on its implementation or its UI.
 */
@JvmInline
value class Destination(val route: String) {
    init {
        require(route.isNotBlank()) { "A destination route must not be blank." }
    }
}
