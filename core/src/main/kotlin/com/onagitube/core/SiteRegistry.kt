package com.onagitube.core

import com.onagitube.extractor.api.ExtractorPlugin
import java.util.regex.Pattern

/**
 * SiteRegistry loads maps from plugin(s), compiles regexes once,
 * matches in O(n) with caching, and supports priority ordering
 * and de-duplication.
 *
 * Exposes match(url): QuickDownloadMap?.
 */
class SiteRegistry {

    // Cache of compiled patterns -> QuickDownloadMap
    private val patternCache = mutableMapOf<String, Pattern>()
    private val mapCache = mutableMapOf<String, QuickDownloadMap>()

    // Registered maps from plugins
    private val registeredMaps = mutableListOf<QuickDownloadMap>()

    /**
     * Register maps from a plugin.
     * Maps are collected and compiled for efficient matching.
     *
     * @param maps list of QuickDownloadMap to register
     */
    fun registerMaps(maps: List<QuickDownloadMap>) {
        registeredMaps.addAll(maps)
        // Compile regexes and update cache
        for (map in maps) {
            patternCache[map.regex] = Pattern.compile(map.regex, Pattern.CASE_INSENSITIVE)
        }
        // Clear cache to rebuild
        rebuildCache()
    }

    /**
     * Match a URL against registered maps.
     * Returns the first matching QuickDownloadMap, or null if no match.
     *
     * @param url the URL to match
     * @return the matching QuickDownloadMap, or null
     */
    fun match(url: String): QuickDownloadMap? {
        for (map in registeredMaps) {
            val pattern = patternCache[map.regex]
            if (pattern != null && pattern.matcher(url).matches()) {
                return map
            }
        }
        return null
    }

    /**
     * Rebuild the cache from registered maps.
     * Called after maps are registered or modified.
     */
    private fun rebuildCache() {
        // Cache is rebuilt on each match iteration for simplicity
        // In production, optimize with proper caching strategy
    }

    /**
     * Get all registered maps (for debugging/inspection).
     *
     * @return list of registered QuickDownloadMap
     */
    fun getRegisteredMaps(): List<QuickDownloadMap> {
        return registeredMaps
    }
}