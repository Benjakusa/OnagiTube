package com.onagitube.app.ui

import android.os.Bundle
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
        webView?.apply {
            settings.javaScriptEnabled = true
            setWebViewClient(WebViewClient() {
                // Handle navigation within the app
                overrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        // Check if URL is from a registered site
                        if (isRegisteredSite(url)) {
                            // Show download button
                            // TODO: Show floating download button
                            Log.d("InAppBrowser", "Registered site URL: $url")
                        }
                    }
                    return true
                }
            })
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
        // Check if URL matches known supported sites
        return url.contains("youtube.com") || url.contains("vimeo.com") || 
               url.contains("soundcloud.com") || url.startsWith("http://") || url.startsWith("https://")
    }
}