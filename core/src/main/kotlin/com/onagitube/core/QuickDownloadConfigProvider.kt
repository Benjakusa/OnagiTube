package com.onagitube.core

import com.onagitube.extractor.api.ExtractorPlugin

/**
 * Host class for plugin management.
 * Provides methods for loading plugins, getting extractor instances,
 * and managing plugin configurations.
 *
 * Invoked reflectively by plugins at runtime.
 */
object QuickDownloadConfigProvider {

    const val METHOD_GET = "get"
    const val METHOD_GET_INSTANCE = "getInstance"

    /**
     * Load a plugin from the given ClassLoader.
     * Used for built-in and bundled plugins.
     *
     * @param classLoader the ClassLoader to load the plugin from
     */
    fun loadPlugin(classLoader: ClassLoader) {
        // Plugin loading logic - implemented by runtime
    }

    /**
     * Get the singleton instance of the provider.
     *
     * @return the provider instance
     */
    fun getInstance(): QuickDownloadConfigProvider {
        return this
    }

    /**
     * Get format configurations for a given site.
     *
     * @param siteRegex the site regex pattern
     * @return list of available formats
     */
    fun getFormatConfigs(siteRegex: String): List<QuickDownloadFormat> {
        return emptyList()
    }

    /**
     * Process result from an extractor plugin.
     *
     * @param resultJson the JSON result from the plugin
     * @return processed result
     */
    fun getResultFromPlugin(resultJson: String): String {
        return resultJson
    }

    /**
     * Convert site codec to JSON.
     *
     * @param codec the site codec
     * @return JSON representation
     */
    fun siteCodecToJson(codec: String): String {
        return """{"codec":"$codec"}"""
    }
}

/**
 * Data class for format information.
 */
data class QuickDownloadFormat(
    val alias: String,
    val ext: String,
    val mime: String,
    val tag: String
)

/**
 * Data class for format map containing regex and formats.
 */
data class QuickDownloadMap(
    val regex: String,
    val formats: Set<QuickDownloadFormat>
)