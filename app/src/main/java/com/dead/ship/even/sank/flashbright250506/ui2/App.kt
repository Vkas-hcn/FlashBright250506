package com.dead.ship.even.sank.flashbright250506.ui2

import android.app.Activity
import android.app.Application

class App : Application() {
    companion object {
        lateinit var localStorage: LocalStorage
        const val dialogState = "isFirst"
         var lastPausedActivity: Activity? = null

    }

    override fun onCreate() {
        super.onCreate()
        localStorage = LocalStorage(this)
        ReliableHomeButtonDetection.initialize(this)

    }
}