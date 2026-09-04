package com.phundal.numverify.common.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Renders whichever registered [FeatureEntry] the current [Destination] points at, and gives every
 * entry a [Navigator] that can reach the others.
 *
 * This is the only Compose-aware part of the navigation contract; everything a feature compiles
 * against ([Destination], [Navigator], [FeatureEntry]) is plain Kotlin. Swapping this host for a
 * Navigation-Compose graph later is therefore a change to this file alone.
 *
 * @param start the destination shown first — the host decides which feature the app opens on.
 * @param entries every screen the installed features contribute.
 */
@Composable
fun FeatureNavHost(
    start: Destination,
    entries: Set<FeatureEntry>,
    modifier: Modifier = Modifier,
) {
    val byDestination = remember(entries) { entries.associateBy(FeatureEntry::destination) }
    val backStack: MutableState<BackStack> = remember(start) { mutableStateOf(BackStack.of(start)) }
    val navigator = remember(backStack) { BackStackNavigator(backStack) }

    BackHandler(enabled = backStack.value.canNavigateBack) { navigator.navigateBack() }

    val current = backStack.value.current
    val entry = checkNotNull(byDestination[current]) {
        "No FeatureEntry is registered for `${current.route}`. Install the module that contributes " +
            "it, or check the route constant the caller navigated to."
    }

    Box(modifier) {
        entry.Content(navigator)
    }
}

/** Moves the host's [BackStack] state. Kept internal: features only ever see [Navigator]. */
internal class BackStackNavigator(
    private val state: MutableState<BackStack>,
) : Navigator {

    override fun navigateTo(destination: Destination) {
        state.value = state.value.push(destination)
    }

    override fun navigateBack(): Boolean {
        val popped = state.value.pop()
        if (popped == state.value) return false
        state.value = popped
        return true
    }
}
