package com.onagitube.extractor.builtin

import com.onagitube.extractor.api.ExtractorPlugin
import kotlinx.serialization.Json
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Direct file extractor - for direct download URLs.
 * Supports: .mp4, .webm, .mkv, .mp3, .m4a, .ogg, .flac, .wav, .pdf, .zip, .jpg, .png
 *
 * Performs a HEAD request to read Content-Type, Content-Length, Accept-Ranges,
 * and filename from Content-Disposition.
 */
class DirectFileExtractor : ExtractorPlugin() {

    override fun loadPlugin(classLoader: ClassLoader) {
        // Built-in plugin - no special loading needed
    }

    override fun get(requestJson: String): String {
        // Parse the request JSON to extract URL
        val url = parseRequestUrl(requestJson)
        if (url == null) {
            return serializeError("Invalid request: missing URL")
        }

        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 30000
            connection.readTimeout = 30000
            connection.connect()

            val contentType = connection.contentType
            val contentLength = connection.contentLength
            val acceptRanges = connection.getHeaderField("Accept-Ranges")
            val contentDisposition = connection.getHeaderField("Content-Disposition")
            val filename = extractFilename(contentDisposition)

            val result = mapOf(
                "type" to "direct_file",
                "url" to url,
                "filename" to filename,
                "contentType" to contentType,
                "contentLength" to contentLength,
                "acceptRanges" to acceptRanges
            )

            return serializeResult(result)

        } catch (e: Exception) {
            return serializeError("Direct file extraction error: ${e.message}")
        }
    }

    private fun parseRequestUrl(requestJson: String): String? {
        try {
            val json = Json.decode(requestJson)
            // Simple extraction - in production use proper serialization
            val map = json.toMap()
            return map["url"] as? String
        } catch (e: Exception) {
            return null
        }
    }

    private fun extractFilename(contentDisposition: String?): String? {
        if (contentDisposition != null) {
            // Try to extract filename from Content-Disposition header
            val cdPattern = """filename=["']?([^"';]+)["']?"""
            val matcher = java.util.regex.Pattern(cdPattern).matcher(contentDisposition)
            return if (matcher.find()) matcher.group(1) else null
        }
        return null
    }

    private fun serializeResult(result: Map<String, Any?>): String {
        return Json.encodeToString(result)
    }

    private fun serializeError(message: String): String {
        return Json.encodeToString(mapOf("error" to message))
    }

    // Serialization helpers for kotlinx.serialization
    @javax.annotation.Nullable
    internal fun encodeToString(value: Map<String, Any?>): String {
        return kotlinx.serialization.json.Json.encodeToString(value)
    }
}