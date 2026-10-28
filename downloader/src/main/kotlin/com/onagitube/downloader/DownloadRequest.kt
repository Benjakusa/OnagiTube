package com.onagitube.downloader

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * DownloadRequest (app-level): url, referrer, userAgent, headers: Map<String,String>,
 * metadata: Map<String,String> (app-only, never sent), verifyType: NONE|MD5|SHA256,
 * expectedDigest, contentType, targetDir, fileName, allowMobileData, priority.
 */
@Serializable
data class DownloadRequest(
    val url: String,
    val referrer: String = "",
    val userAgent: String = "OnagiTube/1.0",
    val headers: Map<String, String> = mapOf(),
    val metadata: Map<String, String> = mapOf(), // app-only, never sent over network
    val verifyType: VerifyType = VerifyType.NONE,
    val expectedDigest: String? = null,
    val contentType: String? = null,
    val targetDir: String = System.getProperty("java.io.tmpdir"),
    val fileName: String? = null,
    val allowMobileData: Boolean = false,
    val priority: Int = DownloadPriority.NORMAL
) {

    /** Priority levels for download scheduling. */
    @Serializable
    enum class DownloadPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    /** Verification types for download integrity. */
    @Serializable
    enum class VerifyType {
        NONE,
        MD5,
        SHA256
    }

    /** Maximum retries for failed downloads. */
    val maxRetries = 3

    /** Whether this is a resumable download. */
    val isResumable: get() = fileName != null && targetDir.isNotEmpty()

    /**
     * Create a JSON representation for network transmission.
     * Metadata is excluded (app-only).
     */
    fun toNetworkJson(): String = Json.encodeToString(
        mapOf(
            "url" to url,
            "referrer" to referrer,
            "userAgent" to userAgent,
            "headers" to headers,
            "verifyType" to verifyType.name,
            "expectedDigest" to expectedDigest,
            "contentType" to contentType,
            "targetDir" to targetDir,
            "fileName" to fileName,
            "allowMobileData" to allowMobileData,
            "priority" to priority.name
        )
    )

    /**
     * Parse a DownloadRequest from JSON.
     */
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun fromNetworkJson(json: String): DownloadRequest {
            val map = Json.decodeFromMap(json) as Map<String, Any>
            return DownloadRequest(
                url = map["url"] as String,
                referrer = map["referrer"] as? String ?? "",
                userAgent = map["userAgent"] as? String ?: "OnagiTube/1.0",
                headers = (map["headers"] as? Map<*, *>)?.toMap<String, String>() ?: mapOf(),
                metadata = mapOf(), // Not sent over network
                verifyType = when (map["verifyType"] as? String) {
                    "MD5" -> VerifyType.MD5
                    "SHA256" -> VerifyType.SHA256
                    else -> VerifyType.NONE
                },
                expectedDigest = map["expectedDigest"] as? String,
                contentType = map["contentType"] as? String,
                targetDir = map["targetDir"] as? String ?: System.getProperty("java.io.tmpdir"),
                fileName = map["fileName"] as? String,
                allowMobileData = (map["allowMobileData"] as? Boolean) ?: false,
                priority = when (map["priority"] as? String) {
                    "LOW" -> DownloadPriority.LOW
                    "HIGH" -> DownloadPriority.HIGH
                    else -> DownloadPriority.NORMAL
                }
            )
        }
    }
}

/**
 * DownloadResponse received from the download server/extractor.
 */
@Serializable
data class DownloadResponse(
    val status: Status,
    val contentLength: Long?,
    val contentRange: String?,
    val etag: String?,
    val lastModified: Long?,
    val mimeType: String?
) {

    @Serializable
    enum class Status {
        OK,
        PARTIAL_CONTENT,
        ERROR,
        NOT_MODIFIED
    }
}