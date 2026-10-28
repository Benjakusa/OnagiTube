package com.onagitube.app.ui

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ClipboardMonitor(context: Context) : LifecycleObserver {

    private val clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var lastClipboardText = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startMonitoring() {
        checkClipboard()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopMonitoring() {
        // Nothing to do
    }

    private fun checkClipboard() {
        val currentText = try {
            if (clipboardManager.hasPrimaryClip()) {
                clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        if (currentText != null && currentText != lastClipboardText) {
            lastClipboardText = currentText
            if (isSupportedUrl(currentText)) {
                Log.d("ClipboardMonitor", "Supported URL detected: $currentText")
            }
        }
    }

    private fun isSupportedUrl(url: String): Boolean =
        url.startsWith("http://") || url.startsWith("https://")
}
