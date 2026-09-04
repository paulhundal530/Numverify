package com.phundal.numverify.feature.numverify.ui

import com.phundal.numverify.common.navigation.Destination
import com.phundal.numverify.common.navigation.FeatureEntry
import com.phundal.numverify.feature.numverify.api.NumverifyDestinations
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.dsl.koinApplication

/**
 * The host finds this feature's screens by asking Koin for every [FeatureEntry], so the binding
 * being a `FeatureEntry` — and not a `NumverifyFeatureEntry` — is what keeps `:app` from having to
 * name it. Getting that wrong compiles fine and only fails when the app is launched, which is
 * exactly the kind of mistake worth a test.
 */
class NumverifyUiModuleTest {

    @Test
    fun `contributes exactly one feature entry, bound as FeatureEntry`() {
        val koin = koinApplication { modules(numverifyUiModule) }.koin

        val entries = koin.getAll<FeatureEntry>()

        assertEquals(1, entries.size)
        assertEquals(
            Destination(NumverifyDestinations.VERIFY_NUMBER),
            entries.single().destination,
        )
    }
}
