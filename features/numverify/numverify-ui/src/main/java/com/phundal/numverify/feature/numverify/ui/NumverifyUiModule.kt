package com.phundal.numverify.feature.numverify.ui

import com.phundal.numverify.common.navigation.FeatureEntry
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * The NumVerify feature's UI bindings.
 *
 * [NumverifyFeatureEntry] is bound as a [FeatureEntry] rather than as itself, so the host can
 * collect every installed feature's screens without knowing any of their types.
 */
val numverifyUiModule = module {

    viewModel {
        VerificationViewModel(
            countriesRepository = get(),
            validationRepository = get(),
        )
    }

    single<FeatureEntry> { NumverifyFeatureEntry() }
}
