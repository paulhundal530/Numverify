package com.phundal.numverify.common.navigation

/**
 * Handed to a feature's UI so it can move to another feature without depending on it.
 *
 * A feature knows the [Destination] it wants and nothing else — not who implements it, not whether
 * it is even installed. Resolving destinations to screens is the host's job.
 */
interface Navigator {

    /** Shows [destination]. Does nothing if it is already the current screen. */
    fun navigateTo(destination: Destination)

    /**
     * Returns to the previous destination.
     *
     * @return `false` when this is the first destination and there is nothing to go back to.
     */
    fun navigateBack(): Boolean
}
