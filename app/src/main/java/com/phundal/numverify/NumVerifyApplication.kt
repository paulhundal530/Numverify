package com.phundal.numverify

import android.app.Application
import com.phundal.numverify.common.networking.networkingModule
import com.phundal.numverify.feature.numverify.impl.numverifyModule
import com.phundal.numverify.feature.numverify.ui.numverifyUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NumVerifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@NumVerifyApplication)
            modules(
                appModule,
                networkingModule,
                // A feature is installed as a pair: `impl` supplies the behaviour, `ui` supplies
                // the screens. Adding a second feature is two more lines here and nothing else.
                numverifyModule,
                numverifyUiModule,
            )
        }
    }
}
