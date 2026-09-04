package com.phundal.numverify.feature.numverify.ui

import androidx.compose.runtime.Composable
import com.phundal.numverify.common.navigation.Destination
import com.phundal.numverify.common.navigation.FeatureEntry
import com.phundal.numverify.common.navigation.Navigator
import com.phundal.numverify.feature.numverify.api.NumverifyDestinations

/**
 * This feature's contribution to the app's navigation graph.
 *
 * The host discovers it through dependency injection, so `:app` never names the screen it renders
 * — it only decides which [Destination] the app opens on.
 */
class NumverifyFeatureEntry : FeatureEntry {

    override val destination: Destination = Destination(NumverifyDestinations.VERIFY_NUMBER)

    @Composable
    override fun Content(navigator: Navigator) {
        // `navigator` is unused for now: this feature has a single screen and nowhere else to go.
        // It stays in the signature so that reaching another feature later needs no host changes.
        VerificationPage()
    }
}
