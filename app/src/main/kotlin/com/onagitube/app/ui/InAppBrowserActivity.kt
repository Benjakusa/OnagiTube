package com.onagitube.app.ui

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.onagitube.app.R

class InAppBrowserActivity : AppCompatActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)

        webView = findViewById(R.id.webView)
        val urlExtra = intent.getStringExtra("url")
        if (urlExtra != null) { webView?.loadUrl(urlExtra) }
        webView?.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && isRegisteredSite(url)) {
                        Log.d("InAppBrowser", "Registered site URL: $url")
                        // TODO: Show floating download button
                    }
                    return false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onDestroy() {
        webView?.destroy()
        super.onDestroy()
    }

    private fun isRegisteredSite(url: String): Boolean {
        return url.contains("youtube.com") || url.contains("vimeo.com") ||
            url.contains("soundcloud.com") || url.startsWith("http://") ||
            url.startsWith("https://")
    }
}
