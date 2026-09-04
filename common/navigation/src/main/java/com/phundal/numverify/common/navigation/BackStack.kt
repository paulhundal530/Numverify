package com.phundal.numverify.common.navigation

/**
 * An immutable navigation history.
 *
 * Kept free of Compose and of Android so the navigation rules can be unit tested directly; the
 * host holds one of these in Compose state and replaces it on every move.
 */
@ConsistentCopyVisibility
data class BackStack private constructor(val entries: List<Destination>) {

    /** The destination currently on screen. */
    val current: Destination get() = entries.last()

    val canNavigateBack: Boolean get() = entries.size > 1

    /** Pushes [destination], unless it is already on top — tapping twice should not stack twice. */
    fun push(destination: Destination): BackStack =
        if (current == destination) this else BackStack(entries + destination)

    /** Pops the top entry, or returns `this` unchanged when only the start destination is left. */
    fun pop(): BackStack =
        if (canNavigateBack) BackStack(entries.dropLast(1)) else this

    companion object {
        /** A history containing only [start]. */
        fun of(start: Destination): BackStack = BackStack(listOf(start))
    }
}
