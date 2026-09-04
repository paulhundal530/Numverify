package com.phundal.numverify.common.navigation

import androidx.compose.runtime.Composable

/**
 * One screen contributed by a feature's `ui` module.
 *
 * Features register their entries with dependency injection; the host collects every binding and
 * renders whichever one the current [Destination] points at. Adding a feature to the app is
 * therefore a matter of installing its module — no host code changes, and no dependency from the
 * host onto the feature's internals.
 */
interface FeatureEntry {

    /** The destination this entry answers to. */
    val destination: Destination

    /** Renders the screen. [navigator] is how this feature reaches any other feature. */
    @Composable
    fun Content(navigator: Navigator)
}
