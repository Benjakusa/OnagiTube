package com.onagitube.extractor.builtin

import com.onagitube.extractor.api.ExtractorPlugin
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

/**
 * Direct file extractor - for direct download URLs.
 * Supports: .mp4, .webm, .mkv, .mp3, .m4a, .ogg, .flac, .wav, .pdf, .zip, .jpg, .png
 *
 * Performs a HEAD request to read Content-Type, Content-Length, Accept-Ranges,
 * and filename from Content-Disposition.
 */
class DirectFileExtractor : ExtractorPlugin {

    override fun loadPlugin(classLoader: ClassLoader) {
        // Built-in plugin - no special loading needed
    }

    override fun get(requestJson: String): String {
        val url = parseRequestUrl(requestJson)
        if (url == null) {
            return serializeError("Invalid request: missing URL")
        }

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 30_000
            connection.readTimeout = 30_000
            connection.connect()

            try {
                val contentType = connection.contentType
                val contentLength = connection.contentLength.toLong()
                val acceptRanges = connection.getHeaderField("Accept-Ranges")
                val contentDisposition = connection.getHeaderField("Content-Disposition")
                val filename = extractFilename(contentDisposition)

                serializeResult(
                    mapOf(
                        "type" to "direct_file",
                        "url" to url,
                        "filename" to filename,
                        "contentType" to contentType,
                        "contentLength" to contentLength,
                        "acceptRanges" to acceptRanges,
                    )
                )
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            serializeError("Direct file extraction error: ${e.message}")
        }
    }

    private fun parseRequestUrl(requestJson: String): String? {
        return try {
            val json = Json.parseToJsonElement(requestJson).jsonObject
            json["url"]?.jsonPrimitive?.content
        } catch (e: Exception) {
            null
        }
    }

    private fun extractFilename(contentDisposition: String?): String? {
        if (contentDisposition == null) return null
        val cdPattern = """filename=["']?([^"';]+)["']?"""
        val matcher = Pattern.compile(cdPattern, Pattern.CASE_INSENSITIVE)
            .matcher(contentDisposition)
        return if (matcher.find()) matcher.group(1) else null
    }

    private fun serializeResult(result: Map<String, Any?>): String =
        buildJsonObject {
            result.forEach { (k, v) ->
                when (v) {
                    null -> put(k, kotlinx.serialization.json.JsonNull)
                    is String -> put(k, v)
                    is Long -> put(k, v)
                    is Int -> put(k, v)
                    is Boolean -> put(k, v)
                    else -> put(k, v.toString())
                }
            }
        }.toString()

    private fun serializeError(message: String): String =
        buildJsonObject { put("error", message) }.toString()
}
