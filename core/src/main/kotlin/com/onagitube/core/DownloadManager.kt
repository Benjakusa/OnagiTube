package com.onagitube.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flowMutableStateFlow
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentHashMap

/**
 * DownloadManager (singleton, Hilt-injected):
 * - Lookup: findByKey(String): DownloadInfo?, findById(Long): DownloadInfo?
 * - Enqueue: enqueue(DownloadRequest): DownloadInfo, enqueueAsync(DownloadRequest)
 * - Control: pause(id, reason: PauseReason), resume(id), cancel(id), retry(id),
 *   delete(id, deleteFile), pauseAll, resumeAll
 * - PauseReason: USER, NO_NETWORK, WIFI_ONLY, LOW_STORAGE, LOW_BATTERY, SYSTEM
 * - Status queries: isRunning, isPaused, isFinished, isFailed
 * - Content-URI handling: handleContentUri(Uri) to open/play/share finished downloads
 *   and to import files via SAF
 * - Policy decisions live here (what to enqueue, when to pause); the transport only executes
 * - Emits a StateFlow<List<DownloadInfo>> and per-task Flow<Progress>
 */
class DownloadManager {

    // In-memory storage for downloads (in production, would use Room + DataStore)
    private val downloads = ConcurrentHashMap<String, DownloadInfo>()
    private val keyToId = ConcurrentHashMap<String, Long>()

    // Status flows (in production, would use SharedFlow / StateFlow from Hilt)
    private val _allDownloads = mutableStateFlow<List<DownloadInfo>>(emptyList())
    val allDownloads: StateFlow<List<DownloadInfo>> = _allDownloads.asStateFlow()

    // Pause reasons
    @Serializable
    enum class PauseReason {
        USER,
        NO_NETWORK,
        WIFI_ONLY,
        LOW_STORAGE,
        LOW_BATTERY,
        SYSTEM
    }

    /** Download status */
    @Serializable
    data class DownloadInfo(
        val id: String,
        val request: DownloadRequest,
        val status: DownloadStatus,
        val progress: Double = 0.0,
        val speed: Long? = null, // bytes per second
        val eta: Long? = null, // seconds
        val error: String? = null,
        val pauseReason: PauseReason? = null,
        val storagePath: String? = null,
        val downloadTime: Long = System.currentTimeMillis()
    ) {

        @Serializable
        enum class DownloadStatus {
            ENQUEUED,
            DOWNLOADING,
            PAUSED,
            COMPLETED,
            FAILED,
            CANCELLED
        }
    }

    /** Enqueue a new download request */
    fun enqueue(request: DownloadRequest): DownloadInfo {
        val id = generateId()
        val info = DownloadInfo(
            id = id,
            request = request,
            status = DownloadStatus.ENQUEUED
        )
        downloads[id] = info
        keyToId[request.url] = info.id
        updateAllDownloads()
        return info
    }

    fun enqueueAsync(request: DownloadRequest): DownloadInfo = kotlinx.coroutines.withContext(
        kotlinx.coroutines.Dispatchers.IO
    ) {
        enqueue(request)
    }

    /** Lookup by key */
    fun findByKey(key: String): DownloadInfo? {
        return downloads[key]
    }

    /** Lookup by ID */
    fun findById(id: Long): DownloadInfo? = downloads.values().firstOrNull { it.id.toLong() == id }

    /** Pause a download */
    fun pause(id: String, reason: PauseReason) {
        downloads[id]?.copy(status = DownloadStatus.PAUSED, pauseReason = reason)
        updateAllDownloads()
    }

    /** Resume a download */
    fun resume(id: String) {
        downloads[id]?.copy(status = DownloadStatus.DOWNLOADING, pauseReason = null)
        updateAllDownloads()
    }

    /** Cancel a download */
    fun cancel(id: String) {
        downloads[id]?.copy(status = DownloadStatus.CANCELLED)
        updateAllDownloads()
    }

    /** Retry a failed download */
    fun retry(id: String) {
        downloads[id]?.copy(status = DownloadStatus.ENQUEUED, error = null, pauseReason = null)
        updateAllDownloads()
    }

    /** Delete a download and optionally the file */
    fun delete(id: String, deleteFile: Boolean) {
        val info = downloads.remove(id)
        keyToId.remove(info?.request.url)
        if (deleteFile && info.storagePath?.isNotEmpty() == true) {
            // TODO: Delete file from storage
        }
        updateAllDownloads()
    }

    /** Pause all downloads */
    fun pauseAll() {
        downloads.values.forEach { it.copy(status = DownloadStatus.PAUSED) }
        updateAllDownloads()
    }

    /** Resume all downloads */
    fun resumeAll() {
        downloads.values.forEach { it.copy(status = DownloadStatus.DOWNLOADING, pauseReason = null) }
        updateAllDownloads()
    }

    /** Update the all-downloads flow */
    private fun updateAllDownloads() {
        val list = downloads.values().toList()
        _allDownloads.value = list
    }

    /** Handle content URI for finished downloads */
    fun handleContentUri(uri: android.net.Uri): String {
        // TODO: Implement SAF file access and return intent action
        return "handled"
    }
}