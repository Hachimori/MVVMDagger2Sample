package com.github.hachimori.mvvmdagger2sample

import android.app.Application
import timber.log.Timber

class MvvmDagger2SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}