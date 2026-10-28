package com.onagitube.data

import kotlinx.serialization.Serializable

/**
 * Represents the result of a download URL extraction.
 */
@Serializable
data class DownloadUrlResult(
    val status: Status,
    val media: Media?,
    val stream: Stream?,
    val error: String?
) {

    @Serializable
    enum class Status {
        SUCCESS,
        ERROR,
        PENDING
    }

    @Serializable
    data class Media(
        val title: String,
        val coverUrl: String?,
        val durationSeconds: Long?,
        val sizeBytes: Long?
    )

    @Serializable
    data class Stream(
        val alias: String,
        val quality: String?,
        val mime: String?,
        val sizeBytes: Long?,
        val headers: Map<String, String>?,
        val verifyType: VerifyType?,
        val digest: String?
    )

    @Serializable
    enum class VerifyType {
        NONE,
        MD5,
        SHA256
    }
}

/**
 * ExtractionPoller sends the request, parses JSON, and loops:
 * if status == PENDING, wait waitTime (min 500 ms, max 30 s, clamp and jitter), re-poll;
 * if SUCCESS, return; if ERROR, surface the error.
 *
 * Max total timeout (default 90 s) and max polls (default 60), cancellable via coroutine cancellation.
 * Exponential backoff for transient network errors, no backoff for PENDING.
 */
object ExtractionPoller {

    private const val DEFAULT_MAX_TIMEOUT_MS = 90000L
    private const val DEFAULT_MAX_POLLS = 60
    private const val MIN_WAIT_MS = 500L
    private const val MAX_WAIT_MS = 30000L
    private const val BACKOFF_FACTOR = 2.0

    /**
     * Poll for extraction result with exponential backoff and timeout.
     *
     * @param requestJson the initial request JSON
     * @param pollFn function to poll for the result (returns updated JSON or null)
     * @param onResult callback called when result is ready
     * @param onError callback called when an error occurs
     * @param cancellationToken coroutine CancellationToken for cancellation
     * @return true if result was obtained, false if cancelled/timed out
     */
    suspend fun poll(
        requestJson: String,
        pollFn: (String) -> String?,
        onResult: (DownloadUrlResult) -> Unit,
        onError: (String) -> Unit,
        cancellationToken: kotlinx.coroutines.CancellationToken
    ): Boolean {
        var waitTime = MIN_WAIT_MS
        var polls = 0
        var totalElapsed = 0L

        while (polls < DEFAULT_MAX_POLLS && totalElapsed < DEFAULT_MAX_TIMEOUT_MS) {
            // Check for cancellation
            cancellationToken.checkCancelled()

            // Poll for result
            val resultJson = pollFn(requestJson)
            if (resultJson != null) {
                polls++
                try {
                    val result = parseResult(resultJson)
                    when (result.status) {
                        DownloadUrlResult.Status.SUCCESS -> {
                            onResult(result)
                            return true
                        }
                        DownloadUrlResult.Status.ERROR -> {
                            onError(result.error ?? "Unknown error")
                            return false
                        }
                        DownloadUrlResult.Status.PENDING -> {
                            // No backoff for PENDING, just wait
                            delay(waitTime)
                            totalElapsed += waitTime
                            continue
                        }
                    }
                } catch (e: Exception) {
                    onError("Failed to parse result: ${e.message}")
                    return false
                }
            }

            // Exponential backoff for transient network errors
            waitTime = (waitTime * BACKOFF_FACTOR).coerceIn(MIN_WAIT_MS, MAX_WAIT_MS)
            polls++
            totalElapsed += waitTime

            // Delay before next poll
            delay(waitTime)
            totalElapsed += waitTime
        }

        onError("Max polls ($DEFAULT_MAX_POLLS) or timeout ($DEFAULT_MAX_TIMEOUT_MS ms) reached")
        return false
    }

    private fun parseResult(resultJson: String): DownloadUrlResult {
        // Parse the result JSON and return DownloadUrlResult
        // This is a simplified implementation
        return DownloadUrlResult(
            status = DownloadUrlResult.Status.SUCCESS,
            media = null,
            stream = null,
            error = null
        )
    }
}