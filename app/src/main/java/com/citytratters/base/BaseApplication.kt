package com.citytratters.base

import android.app.Application
import android.util.Log
import com.citytratters.constants.MyConfig.APPSETTING.FLURRY_API_KEY
import com.citytratters.di.appModule
import com.citytratters.di.networkModule
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        // start Kotlin!
        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule, networkModule)
        }

        FlurryAgent.Builder()
            .withDataSaleOptOut(false) //CCPA - the default value is false
            .withCaptureUncaughtExceptions(true)
            .withIncludeBackgroundSessionsInMetrics(true)
            .withLogLevel(Log.VERBOSE)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .build(this, FLURRY_API_KEY)

    }


    companion object {

        private lateinit var sInstance: BaseApplication

        fun getInstance() = sInstance
    }
}