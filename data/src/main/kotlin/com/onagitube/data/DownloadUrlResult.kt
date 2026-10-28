package com.onagitube.data

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

/**
 * Represents the result of a download URL extraction.
 */
@Serializable
data class DownloadUrlResult(
    val status: Status,
    val media: Media?,
    val stream: Stream?,
    val error: String?,
) {

    enum class Status {
        SUCCESS,
        ERROR,
        PENDING,
    }

    @Serializable
    data class Media(
        val title: String,
        val coverUrl: String?,
        val durationSeconds: Long?,
        val sizeBytes: Long?,
    )

    @Serializable
    data class Stream(
        val alias: String,
        val quality: String?,
        val mime: String?,
        val sizeBytes: Long?,
        val headers: Map<String, String>?,
        val verifyType: VerifyType?,
        val digest: String?,
    )

    enum class VerifyType {
        NONE,
        MD5,
        SHA256,
    }
}

/**
 * ExtractionPoller sends the request, parses JSON, and loops:
 *  - if status == PENDING, wait waitTime (min 500 ms, max 30 s, clamped, with jitter), re-poll;
 *  - if SUCCESS, return; if ERROR, surface the error.
 *
 * Max total timeout (default 90 s) and max polls (default 60), cancellable via coroutine cancellation
 * of the caller's scope. Exponential backoff for transient network errors; no backoff for PENDING.
 */
object ExtractionPoller {

    private const val DEFAULT_MAX_TIMEOUT_MS = 90_000L
    private const val DEFAULT_MAX_POLLS = 60
    private const val MIN_WAIT_MS = 500L
    private const val MAX_WAIT_MS = 30_000L
    private const val BACKOFF_FACTOR = 2.0

    /**
     * Poll for extraction result with exponential backoff and timeout.
     *
     * Cancellation is handled by the caller via standard coroutine cancellation
     * (e.g. `withTimeout { ... }` or cancelling the surrounding `Job`); this function
     * will throw [kotlinx.coroutines.CancellationException] if the calling scope is cancelled.
     *
     * @param requestJson the initial request JSON
     * @param pollFn function to poll for the result (returns updated JSON or null)
     * @param onResult callback called when result is ready
     * @param onError callback called when an error occurs
     * @return true if result was obtained, false if timed out / max polls reached
     */
    suspend fun poll(
        requestJson: String,
        pollFn: (String) -> String?,
        onResult: (DownloadUrlResult) -> Unit,
        onError: (String) -> Unit,
    ): Boolean {
        var waitTime = MIN_WAIT_MS
        var polls = 0
        var totalElapsed = 0L

        while (polls < DEFAULT_MAX_POLLS && totalElapsed < DEFAULT_MAX_TIMEOUT_MS) {
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
                            onError(result.error ?: "Unknown error")
                            return false
                        }
                        DownloadUrlResult.Status.PENDING -> {
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

            waitTime = (waitTime * BACKOFF_FACTOR)
                .toLong()
                .coerceIn(MIN_WAIT_MS, MAX_WAIT_MS)
            polls++
            delay(waitTime)
            totalElapsed += waitTime
        }

        onError("Max polls ($DEFAULT_MAX_POLLS) or timeout ($DEFAULT_MAX_TIMEOUT_MS ms) reached")
        return false
    }

    private fun parseResult(resultJson: String): DownloadUrlResult {
        // TODO: parse `resultJson` into a DownloadUrlResult
        return DownloadUrlResult(
            status = DownloadUrlResult.Status.SUCCESS,
            media = null,
            stream = null,
            error = null,
        )
    }
}
