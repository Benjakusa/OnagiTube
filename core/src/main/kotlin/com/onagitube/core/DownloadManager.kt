package com.onagitube.core

import com.onagitube.downloader.DownloadRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * DownloadManager (singleton, Hilt-injected in production).
 * Manages in-memory download state; intended as the policy layer.
 */
class DownloadManager {

    private val downloads = ConcurrentHashMap<String, DownloadInfo>()
    private val keyToId = ConcurrentHashMap<String, String>()

    private val _allDownloads = MutableStateFlow<List<DownloadInfo>>(emptyList())
    val allDownloads: StateFlow<List<DownloadInfo>> = _allDownloads.asStateFlow()

    enum class PauseReason {
        USER,
        NO_NETWORK,
        WIFI_ONLY,
        LOW_STORAGE,
        LOW_BATTERY,
        SYSTEM,
    }

    data class DownloadInfo(
        val id: String,
        val request: DownloadRequest,
        val status: DownloadStatus,
        val progress: Double = 0.0,
        val speed: Long? = null,
        val eta: Long? = null,
        val error: String? = null,
        val pauseReason: PauseReason? = null,
        val storagePath: String? = null,
        val downloadTime: Long = System.currentTimeMillis(),
    ) {
        enum class DownloadStatus {
            ENQUEUED,
            DOWNLOADING,
            PAUSED,
            COMPLETED,
            FAILED,
            CANCELLED,
        }
    }

    fun enqueue(request: DownloadRequest): DownloadInfo {
        val id = generateId()
        val info = DownloadInfo(
            id = id,
            request = request,
            status = DownloadInfo.DownloadStatus.ENQUEUED,
        )
        downloads[id] = info
        keyToId[request.url] = id
        updateAllDownloads()
        return info
    }

    suspend fun enqueueAsync(request: DownloadRequest): DownloadInfo =
        withContext(Dispatchers.IO) { enqueue(request) }

    fun findByKey(key: String): DownloadInfo? {
        val id = keyToId[key] ?: return null
        return downloads[id]
    }

    fun findById(id: String): DownloadInfo? = downloads[id]

    fun pause(id: String, reason: PauseReason) {
        downloads[id]?.let {
            downloads[id] = it.copy(
                status = DownloadInfo.DownloadStatus.PAUSED,
                pauseReason = reason,
            )
            updateAllDownloads()
        }
    }

    fun resume(id: String) {
        downloads[id]?.let {
            downloads[id] = it.copy(
                status = DownloadInfo.DownloadStatus.DOWNLOADING,
                pauseReason = null,
            )
            updateAllDownloads()
        }
    }

    fun cancel(id: String) {
        downloads[id]?.let {
            downloads[id] = it.copy(status = DownloadInfo.DownloadStatus.CANCELLED)
            updateAllDownloads()
        }
    }

    fun retry(id: String) {
        downloads[id]?.let {
            downloads[id] = it.copy(
                status = DownloadInfo.DownloadStatus.ENQUEUED,
                error = null,
                pauseReason = null,
            )
            updateAllDownloads()
        }
    }

    fun delete(id: String, deleteFile: Boolean) {
        val info = downloads.remove(id)
        info?.let { keyToId.remove(it.request.url) }
        if (deleteFile && info?.storagePath?.isNotEmpty() == true) {
            // TODO: Delete file from storage
        }
        updateAllDownloads()
    }

    fun pauseAll() {
        downloads.keys.toList().forEach { id ->
            downloads[id]?.let {
                downloads[id] = it.copy(
                    status = DownloadInfo.DownloadStatus.PAUSED,
                    pauseReason = PauseReason.USER,
                )
            }
        }
        updateAllDownloads()
    }

    fun resumeAll() {
        downloads.keys.toList().forEach { id ->
            downloads[id]?.let {
                downloads[id] = it.copy(
                    status = DownloadInfo.DownloadStatus.DOWNLOADING,
                    pauseReason = null,
                )
            }
        }
        updateAllDownloads()
    }

    private fun updateAllDownloads() {
        _allDownloads.value = downloads.values.toList()
    }

    private fun generateId(): String = UUID.randomUUID().toString()

    fun handleContentUri(uri: android.net.Uri): String {
        // TODO: Implement SAF file access
        return "handled: $uri"
    }
}
