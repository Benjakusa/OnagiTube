package com.onagitube.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class DownloadService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return when {
            intent == null -> {
                stopSelf()
                START_NOT_STICKY
            }
            intent.action == "com.onagitube.action.START" -> {
                Timber.i("START intent")
                START_STICKY
            }
            else -> {
                Timber.d("Unknown intent: ${intent.action}")
                stopSelf()
                START_NOT_STICKY
            }
        }
    }
}
