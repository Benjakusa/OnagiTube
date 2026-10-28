package com.onagitube.app.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.onagitube.app.R

class ShareTargetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_target)

        // Handle share intent
        val intent = intent
        val intentType = intent.type
        if (intent.action == Intent.ACTION_SEND && intentType != null && intentType.contains("text/plain")) {
            val url = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (url != null) {
                // Validate and enqueue download
                Log.d("ShareTarget", "Received URL via share: $url")
            }
        }
        // Also handle ACTION_VIEW for http/https
        else if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data
            if (data != null) {
                Log.d("ShareTarget", "Received URL via ACTION_VIEW: ${data.toString()}")
            }
        }
    }
}