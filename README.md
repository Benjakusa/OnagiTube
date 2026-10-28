# OnagiTube

A modern Android media downloader and extractor application built with Kotlin.

## Features

- **Direct File Extraction**: Built-in extractor for direct media URLs.
- **Extensible Plugin Architecture**: Supports runtime loading of extractor plugins.
- **In-App Browser**: WebView-based browser with interception for registered media sites.
- **Modern UI**: Snaptube-inspired dark theme with top navigation tabs and a floating search bar.
- **Room Database**: Persistent storage for download records and plugin metadata.
- **Coroutines & Flow**: Fully asynchronous download management and state observation.

## Project Structure

The project is a multi-module Gradle build designed for separation of concerns:

- `:app` - The main Android application (UI, Activities).
- `:core` - Core business logic, download manager, and service.
- `:data` - Room database, entities, DAOs, and data models.
- `:downloader` - Network layer using OkHttp for handling download requests.
- `:extractor-api` - The interface contract for extractor plugins.
- `:extractor-builtin` - The default implementation for direct file extraction.

## Building the Project

### Prerequisites
- **JDK 17** installed.
- **Android SDK** (Platform 34, Build Tools 34.0.0).

### Build Instructions
1. Ensure `local.properties` points to your Android SDK:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```
2. Run the Gradle wrapper to build the debug APK:
   ```bash
   ./gradlew :app:assembleDebug
   ```
3. The APK will be located at:
   `app/build/outputs/apk/debug/app-debug.apk`

### Installation
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
## Architecture Notes

### Dependency Graph
- `:app` depends on all other modules.
- `:core` depends on `:extractor-api` and `:downloader`.
- `:data` depends on `:core`.
- `:extractor-builtin` and `:downloader` depend on `:extractor-api`.

### Serialization
The app uses `kotlinx.serialization` for JSON parsing. Note that `DownloadRequest.metadata` is marked `@Transient` to ensure app-only data is never sent over the network.

### Database (Room)
The `:data` module uses Room with KSP. Entities include `SlogDownload`, `RichDownload`, `Segment`, `DownloadFailRecord`, and `PluginMeta`. To view or modify the schema, check the `data/schemas` directory generated during compilation.

## Current Status & Known Issues

- **Build System**: Repaired. All Gradle configurations, plugins, and dependencies are correctly aligned (AGP 8.2.2, Kotlin 1.9.22).
- **UI**: The home screen and in-app browser are functional. The top tabs ("Search", "YouTube", etc.) and bottom navigation are currently UI-only placeholders.
- **Extraction**: The `DirectFileExtractor` performs HEAD requests to fetch metadata. Actual file downloading logic in `DownloadManager` is pending implementation.
- **Plugin Loader**: Plugin loading via reflection is stubbed and not fully wired up.

## Development Log (Recent Fixes)

This project underwent a major build-system overhaul to get to a compiling state. Key fixes included:
- Moving `libs.versions.toml` to `gradle/` and correcting invalid TOML syntax.
- Fixing `SettingsRepos.MODE_*` hallucinations in `settings.gradle.kts`.
- Converting `:core` and `:data` to Android Libraries (they use `android.*` APIs).
- Fixing numerous Kotlin source errors (invalid `WebViewClient` syntax, missing `@Entity` annotations, incorrect `Json` API usage, and missing `onBind` in the Service).

## License
This project is provided for educational purposes.
