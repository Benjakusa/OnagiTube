package com.onagitube.app.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.onagitube.app.R

class MainActivity : AppCompatActivity() {

    private var urlEditText: EditText? = null
    private var pasteButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.url_edit_text)
        pasteButton = findViewById(R.id.paste_button)

        pasteButton?.setOnClickListener {
            val url = urlEditText?.text?.toString()?.trim()
            if (url != null && !url.isEmpty()) {
                if (isValidUrl(url)) {
                    // TODO: Enqueue download or open share target
                    Toast.makeText(this, "URL validated: $url", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid URL format", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}