package com.onagitube.extractor.api

/**
 * Interface for extractor plugins.
 * Plugins are loaded at runtime and must implement this contract.
 * A crashing plugin must never crash the host - the host must wrap plugin calls in try/catch
 * and have a timeout watchdog.
 */
interface ExtractorPlugin {

    /**
     * Load a plugin from the given ClassLoader.
     * This is used for built-in and bundled plugins.
     *
     * @param classLoader the ClassLoader to load the plugin from
     */
    fun loadPlugin(classLoader: ClassLoader)

    /**
     * Extract media information from the given request JSON.
     * The request JSON typically contains: {"url", "referrer", "tag"}
     *
     * @param requestJson JSON string with extraction request parameters
     * @return JSON string with extraction result (DownloadUrlResult)
     */
    fun get(requestJson: String): String
}