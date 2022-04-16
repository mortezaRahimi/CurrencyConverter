package com.mortex.converter

import android.app.Application
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()
      if( BuildConfig.DEBUG)
          Timber.plant(Timber.DebugTree())

    }
}