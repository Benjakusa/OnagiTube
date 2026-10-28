package com.onagitube.downloader

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

/**
 * DownloadRequest (app-level): url, referrer, userAgent, headers, metadata (app-only),
 * verifyType, expectedDigest, contentType, targetDir, fileName, allowMobileData, priority.
 */
@Serializable
data class DownloadRequest(
    val url: String,
    val referrer: String = "",
    val userAgent: String = "OnagiTube/1.0",
    val headers: Map<String, String> = emptyMap(),
    @Transient
    val metadata: Map<String, String> = emptyMap(),
    val verifyType: VerifyType = VerifyType.NONE,
    val expectedDigest: String? = null,
    val contentType: String? = null,
    val targetDir: String = System.getProperty("java.io.tmpdir") ?: "/tmp",
    val fileName: String? = null,
    val allowMobileData: Boolean = false,
    val priority: DownloadPriority = DownloadPriority.NORMAL,
) {

    @Serializable
    enum class DownloadPriority { LOW, NORMAL, HIGH, URGENT }

    @Serializable
    enum class VerifyType { NONE, MD5, SHA256 }

    val maxRetries: Int get() = 3

    val isResumable: Boolean get() = fileName != null && targetDir.isNotEmpty()

    /** Serialize for network transfer (metadata is @Transient, auto-excluded). */
    fun toNetworkJson(): String = Json.encodeToString(DownloadRequest.serializer(), this)

    companion object {
        fun fromNetworkJson(json: String): DownloadRequest =
                Json.decodeFromString(DownloadRequest.serializer(), json)
    }
}

@Serializable
data class DownloadResponse(
    val status: Status,
    val contentLength: Long?,
    val contentRange: String?,
    val etag: String?,
    val lastModified: Long?,
    val mimeType: String?,
) {
    @Serializable
    enum class Status { OK, PARTIAL_CONTENT, ERROR, NOT_MODIFIED }
}
