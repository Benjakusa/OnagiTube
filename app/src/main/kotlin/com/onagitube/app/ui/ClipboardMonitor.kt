package com.onagitube.app.ui

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class ClipboardMonitor(context: Context) : LifecycleObserver {

    private val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var lastClipboardText = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startMonitoring() {
        // Check clipboard when app becomes visible
        checkClipboard()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_stop)
    fun stopMonitoring() {
        // Stop monitoring when app is in background
    }

    private fun checkClipboard() {
        val currentText = try {
            if (clipboardManager.hasPrimaryClip) {
                val item = clipboardManager.getPrimaryClip()?.getItemAt(0)
                item?.text?.toString()
            } else null
        } catch (e: Exception) {
            null
        }

        if (currentText != null && currentText != lastClipboardText) {
            lastClipboardText = currentText
            // Validate URL and show chip if supported
            if (isSupportedUrl(currentText)) {
                Log.d("ClipboardMonitor", "Supported URL detected: $currentText")
                // TODO: Show non-intrusive chip to user
            }
        }
    }

    private fun isSupportedUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}