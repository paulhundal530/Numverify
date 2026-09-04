package com.phundal.numverify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.phundal.numverify.common.navigation.Destination
import com.phundal.numverify.common.navigation.FeatureEntry
import com.phundal.numverify.common.navigation.FeatureNavHost
import com.phundal.numverify.feature.numverify.api.NumverifyDestinations
import com.phundal.numverify.ui.theme.NumverifyTheme
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumverifyTheme {
                // Every installed feature's screens, collected without naming any of them.
                val koin = getKoin()
                val entries = remember(koin) { koin.getAll<FeatureEntry>().toSet() }

                FeatureNavHost(
                    start = Destination(NumverifyDestinations.VERIFY_NUMBER),
                    entries = entries,
                )
            }
        }
    }
}
