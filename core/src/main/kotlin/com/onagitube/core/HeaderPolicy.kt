package com.onagitube.core

import kotlinx.serialization.Serializable

/**
 * Single owner class HeaderPolicy producing the header map for each request:
 * - User-Agent (a clear, honest OnagiTube UA string by default; user-overridable)
 * - Referer (from the intake referrer)
 * - Cookie and Authorization only if the user explicitly provided them for that site
 *   (stored encrypted)
 * - Range set only by the block engine
 *
 * Unit-test that headers are copied, not shared by reference, and that sensitive
 * headers are never logged (redact in logs).
 */
@Serializable
data class HeaderPolicy(

    /** OnagiTube User-Agent string */
    val userAgent: String = "OnagiTube/1.0 (compatible; Linux; Android 30)",

    /** Referer header value */
    val referer: String? = null,

    /** Optional Cookie header */
    val cookie: String? = null,

    /** Optional Authorization header */
    val authorization: String? = null,

    /** Optional Range header */
    val range: String? = null
) {

    /** Produce a fresh header map for an HTTP request. */
    fun buildHeaders(): Map<String, String> {
        val headers = mutableMapOf<String, String>()

        // User-Agent - always included
        headers["User-Agent"] = userAgent

        // Referer - if provided
        referer?.let { headers["Referer"] = it }

        // Cookie - if provided (stored encrypted, never logged)
        cookie?.let { headers["Cookie"] = it }

        // Authorization - if provided (stored encrypted, never logged)
        authorization?.let { headers["Authorization"] = it }

        // Range - intentionally not set here; set by block engine if needed

        // Return a copy to ensure headers are not shared by reference
        return headers.toMap()
    }

    /**
     * Create a HeaderPolicy from a request intake (referrer + optional user settings).
     *
     * @param referer the referer URL from URL intake
     * @param userCookie optional cookie from user settings (encrypted)
     * @param userAuth optional authorization from user settings (encrypted)
     * @return a new HeaderPolicy instance
     */
    companion object {
        fun fromIntake(
            referer: String?,
            userCookie: String?,
            userAuth: String?
        ): HeaderPolicy {
            return HeaderPolicy(
                referer = referer,
                cookie = userCookie,
                authorization = userAuth
            )
        }
    }
}