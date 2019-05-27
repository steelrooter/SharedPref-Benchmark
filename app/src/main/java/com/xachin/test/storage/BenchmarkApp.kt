package com.xachin.test.storage

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import timber.log.Timber


private const val TAG = "BenchmarkApp"

class BenchmarkApp : Application() {

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        val appOnCreateTime = now()
        Log.d(TAG, "~~~~ App onCreate called: $appOnCreateTime")

        super.onCreate()

        val superOnCreateDone = now()
        Log.d(TAG, "~~~~ App super.onCreate done: $superOnCreateDone")

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber planted")
        }

    }

}
