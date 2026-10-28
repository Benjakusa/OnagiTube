# OnagiTube — Agent Prompt: Develop and Test the App

You are a senior Android engineer and QA lead. Your job is to design, build, test, and document **OnagiTube**, an Android media downloader, working directly in the project repository. Read this entire prompt before writing any code. Do not skip, summarize, or defer any section. When something is ambiguous, state your assumption in `docs/ASSUMPTIONS.md` and continue.

---

## PART A — Ground rules

### A1. Clean-room and legal guardrails (mandatory)
1. The reference description in Part B was derived from static analysis of an existing app. Use it **only as an architectural blueprint**. Do **not** copy decompiled code, obfuscated class names (`o.yp2`, `o.sw4`, etc.), proprietary SDK code, or assets. Write all code fresh with clear, meaningful names.
2. Do **not** bundle or depend on the proprietary Wandoujia SDK. Implement the transport layer yourself (OkHttp-based, Part C, Module 6).
3. OnagiTube must **not** circumvent DRM, encryption, paywalls, or technical protection measures, and must not impersonate other apps' clients to bypass access controls.
4. Extractor plugins shipped by default must target only sources where downloading is permitted: direct file URLs, user-owned or Creative Commons/public-domain content (e.g. Internet Archive, Wikimedia Commons, Jamendo, Free Music Archive), podcasts/RSS enclosures, and open HLS/DASH streams that are not DRM-protected.
5. Add a first-run **Terms & Responsible Use** screen: users are responsible for having the right to download content; OnagiTube does not endorse violating a site's terms or copyright law. Store acceptance and require it before the first download.
6. Add a **Privacy Policy** screen and a "Report a problem" link.

### A2. Working method
- Work in small, verified increments. After each module: build, run unit tests, commit with a clear message.
- Never claim something works without running it. Report real command output for builds and tests.
- Keep a living checklist at `docs/PROGRESS.md` mapping every requirement in this prompt to status (Not started / In progress / Done / Blocked) and a link to its test.
- If blocked, document the blocker, implement the best fallback, and continue.

### A3. Technology baseline
- Language: **Kotlin** (100%). Min SDK 24, target/compile SDK = latest stable.
- UI: Jetpack Compose + Material 3, single-activity, Navigation Compose.
- Architecture: MVVM + clean layers (`ui`, `domain`, `data`, `core`), Hilt for DI, Kotlin Coroutines + Flow.
- Persistence: Room (SQLite) + DataStore for settings.
- Networking: OkHttp 4/5 + kotlinx.serialization.
- Background work: Foreground Service for active downloads, WorkManager for retries/scheduling.
- Media: Media3 (ExoPlayer) for in-app playback and preview.
- Image loading: Coil.
- Build: Gradle Kotlin DSL, version catalog (`libs.versions.toml`), ktlint/detekt, CI via GitHub Actions.
- Package root: `com.onagitube`.

---

## PART B — Reference description (architectural blueprint)

> Everything below is the technical functional description, with the product name replaced by OnagiTube. Package names containing the old name are shown with the new name as requested. Treat item numbers as requirement IDs (B1–B15).

# OnagiTube — Technical Functional Description

Based on the static analysis performed with REA 6.7.0 + JADX 0.7.1. Every claim is traceable to a decompiled class, field type, prototype, or method body. Inferences and unverified items are marked explicitly.

## B1. Executive summary

OnagiTube is an Android media downloader whose architecture separates **extraction** from **transport**:

- **Extraction** (OnagiTube-owned): a runtime-loaded plugin, driven by regex site-matching, returns URLs asynchronously as JSON.
- **Transport** (third-party Wandoujia SDK in the reference; **in OnagiTube you will implement your own**): receives the resolved URL plus a header map and streams the file to disk with CRC verification.

The two halves meet at a single `String` — `DownloadUrlResult.Stream.downloadUrl`. Everything upstream decides *what* to download; everything downstream decides *how*.

The app's own code is the orchestration layer: `com.onagitube.*` and `com.phoenix.*`. The Wandoujia namespace (`com.wandoujia.*`) is a bundled SDK, and `com.mbridge.*`, `com.vungle.*`, `com.squareup.picasso.*`, and ExoPlayer are unrelated bundled libraries.

## B2. Entry points

The download path begins at `com.onagitube.premium.utils.CopyLinkDownloadUtils` — a utility class with 13 prototype methods. Its two entry shapes are:

- `h(AppCompatActivity, String, String, String, String): void` — interactive path (dialog/picker)
- `d(String, Context, String, boolean, boolean, String): boolean` — background path with two boolean mode switches

The URL is not constructed here. It arrives as Intent extras under the keys **`"full_url"`** and **`"referrer"`**. Source of those intents: clipboard monitor, in-app browser, or share target.

## B3. Site matching and format vocabulary

**`com.onagitube.plugin.extension.download.QuickDownloadMap`** is a Kotlin data class with two fields:

- `regex: String` — a URL pattern identifying a supported site
- `formats: Set<QuickDownloadFormat>` — the formats offered for that site

**`com.onagitube.plugin.extension.download.QuickDownloadFormat`** is a Kotlin data class:

- `alias: String` — display label ("720p MP4", "MP3 320kbps")
- `ext: String` — file extension
- `mime: String` — MIME type
- `tag: String` — internal selector sent to the extractor

Each `QuickDownloadMap` is a registry entry: *when the URL matches `regex`, present `formats` to the user.* The selected `tag` parameterizes the extraction request. The full set of maps constitutes the supported-site catalog (runtime data, not class fields).

## B4. Plugin architecture — the extractor

The extractor logic is **not compiled into the main DEX**. It is loaded at runtime via `ClassLoader` behind a two-method interface.

**Plugin interface** (obfuscated in the reference):
- `get(): String` — returns the extraction result as JSON
- `loadPlugin(ClassLoader): void` — loads the plugin into the supplied classloader

**`com.onagitube.extractor.pluginlib.common.QuickDownloadConfigProvider`** (host side, 8 methods):

- `loadPlugin(ClassLoader): void` — hosts the ClassLoader
- `getInstance(): plugin` — obtains the plugin singleton
- `get(): String` — host-side invocation of the plugin's `get`
- `getFormatConfigs(): List<...>` — retrieves per-site format configs
- `siteCodecToJson(...): String` — serializes site config to the plugin's wire format
- `getResultFormPlugin(): String` — retrieves the plugin's result
- Constants: **`METHOD_GET`**, **`METHOD_GET_INSTANCE`** — reflective method-name strings

**Contract:** the plugin must expose methods named `get` and `getInstance`; the host invokes them reflectively. A two-method contract — minimal, narrow, stable across plugin versions.

**Implication:** site-specific extraction logic (HTML/JSON parsing, signature computation, token extraction) lives inside the plugin, not the main APK. The main APK contains only the loader and the data model.

## B5. The extraction result — async envelope

**`com.onagitube.premium.model.DownloadUrlResult`** is a pure data holder designed for JSON deserialization:

- `status: int` — `STATUS_PENDING` / `STATUS_SUCCESS`
- `waitTime: int` — poll interval
- `vid: String` — site-specific video identifier
- `media: DownloadUrlResult.Media` — the payload

**`DownloadUrlResult.Media`** (4 fields):
- `cover: String` — thumbnail URL
- `title: String` — media title
- `duration: long` — length
- `downloadUrls: List<DownloadUrlResult.Stream>` — the variants

**`DownloadUrlResult.Stream`** (1 field):
- `downloadUrl: String` — the actual media URL

**Extraction is asynchronous and poll-based** (a polling loop, not callbacks or a Future):

```
request -> status = PENDING, waitTime = N
  wait N -> check status
    if PENDING -> repeat
    if SUCCESS -> read Media.downloadUrls
```

A single extraction can return **multiple `Stream` variants** — quality variants, codec variants, or mirror candidates. Selection between them is a UI-layer decision.

## B6. Transport handoff — the join point

The extracted URL (`Stream.downloadUrl`) is handed to `com.phoenix.download.DownloadRequest`. Its 21 fields are populated from the extraction result plus app policy:

- `a: String` — URL
- `c, b: String` — optional strings (referer / user-agent candidates, conditionally set via empty-string guards)
- `k: Map` — request headers
- `e: HashMap` — a second, separate map channel not forwarded to the SDK (purpose unverified)
- `t: VerifyType` — MD5 or PF5
- `u: String` — expected digest
- `f: ContentType` — media content type

The builder has 42 methods and 20 fields. Two map setters (`E(Map)` -> field `j`, `B(HashMap)` -> field `d`) are plain reference assignments with no defensive copy. **Whoever constructs the map owns the header literals.**

**Conversion to the SDK request** (`DownloadRequest` -> `InnerDownloadRequest`):

```java
InnerDownloadRequest.b bVar = new InnerDownloadRequest.b(downloadRequest.a, ContentType->ResourceType(downloadRequest.f));
// URL = .a ; resource type = ContentType -> ResourceType
if (!TextUtils.isEmpty(downloadRequest.c)) bVar.E(downloadRequest.c);
if (!TextUtils.isEmpty(downloadRequest.b)) bVar.J(downloadRequest.b);
if (downloadRequest.t != null && !TextUtils.isEmpty(downloadRequest.u)) {
    // ordinal -> VerifyType.MD5 or PF5, digest = .u
}
return bVar.w(...).x(...).z(...).A(...).B(...).F(...).G(...)
           .C(downloadRequest.k)   // HEADERS MAP
           .I(...).y(...).K(...).D(...).M(...).H(...).v();
```

**Confirmed field mapping:** URL -> `InnerDownloadRequest.a`; Headers -> `.j` (from `DownloadRequest.k`); Verify -> `.t` / `.u`; the `HashMap` channel (`DownloadRequest.e`) is not forwarded.

## B7. SDK transport — the Wandoujia stack (reference only)

- **Scheduler** (22 prototypes, 14 fields): accepts `InnerDownloadRequest` via `d(...)` and `s(...)`, returns an opaque task handle. Fields include a network-state listener, `ExecutorService`, and a `LongSparseArray` task index.
- **`InnerDownloadRequest`** (21 fields, 2 constructors): immutable, built exclusively through its builder (41 methods). Map setters are reference assignments.
- **`HttpStreamDownloadTask`** (37 prototypes, 10 fields) is the byte mover:
  - `h(Map)` — receives the header map
  - `E(byte[], int)` — writes streamed bytes
  - `t(): CrcCalculator` — CRC verification built into the transport
  - `x/z(IOException)` — error handlers
  - `stop()` plus lifecycle methods
- The HTTP client is Java (not JNI).
- **Block/ranged download** uses sibling classes: `SingleThreadBlockDownloadTask`, `IBlockDownloadTask`, and models `DownloadBlockInfo` / `DownloadSegMetaData` / `DownloadConfigInfo`.
- The absent `Range` string literal is explained: range offsets are computed programmatically from segment metadata.

## B8. The orchestration manager

The download manager (56 prototypes, 16 fields). Fields: `ExecutorService`, `AtomicInteger` (worker pool, concurrency), `LongSparseArray` (active-download index), the SDK scheduler, and a retained directory/base string.

Methods span the full lifecycle:
- Lookup: `A(String): DownloadInfo`, `z(long): DownloadInfo`
- Enqueue: `Y/a0(DownloadRequest): DownloadInfo`, `Z(DownloadRequest): void`
- Pause with typed reason: `n(long, PauseReason): boolean`
- Status: `r/t/P/Q(DownloadInfo): boolean`
- Content-URI handling: `X(Uri): void`

It is the boundary between app policy and SDK execution: the manager decides what to enqueue and when to pause; the SDK executes.

## B9. The Android Service

**`com.phoenix.download.DownloadService`** (25 prototypes) is a real `Service`: `onStartCommand(Intent, int, int): int`, `onBind(Intent): IBinder`, `onTimeout(int, int): void`. The dispatcher `j(Intent)` is called from `onStartCommand`. The notification builder `h(): Notification` indicates a foreground service for active downloads. Error entry points: `d(Throwable)`, `o(Throwable)`.

A **second service** — `com.onagitube.premium.localplay.download.BackgroundDownloadService` — handles local-playback downloads, separate from the main path (not traced).

## B10. State tracking

Download state is persisted in SQLite table **`tbl_slog_download`**:

```
_id INTEGER PRIMARY KEY AUTOINCREMENT,
type TEXT, source TEXT, file TEXT, downloadUrl TEXT, addTime INTEGER
```

Progress and status are broadcast via Intent extras: `EXTRA_DOWNLOAD_PROGRESS`, `EXTRA_DOWNLOAD_STATUS`, `CUSTOM_ACTION_DOWNLOAD`, `CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE`.

A `DownloadFailRecords` table stores failed download IDs for retry. UI components (`DownloadItemActionDialog`, `DownloadingTaskFragment`) query it for display.

## B11. Integrity verification — two layers

1. **Stream CRC** — computed during streaming; detects transport corruption.
2. **Post-download digest** — `VerifyType.MD5` or `PF5` plus expected digest; detects server-side or MITM substitution. The expected digest must come from the extraction result (carried in `Media`/`Stream` fields or the plugin's JSON payload).

## B12. Headers

Headers are a `java.util.Map` carried from `DownloadRequest.k` -> `InnerDownloadRequest.j` -> `HttpStreamDownloadTask.h(Map)`. Java-level throughout, no native header code. Header literals present app-wide: `User-Agent`, `Referer`, `Cookie`, `Authorization`, `x-requested-with`. `Range` is absent as a literal because block offsets are computed.

## B13. What the app does not do

- No native HTTP (no `libokhttp.so`, no app-specific `.so`). Transport is Java.
- No OkHttp in the main downloader of the reference (OkHttp appears only in ad and image SDKs).
- No runtime signature verification by REA; the APK was never executed.

## B14. Three download subsystems

1. **Main download path** (Phoenix/Wandoujia in the reference) — user-requested media.
2. **`localplay` / extractor plugin** — `BackgroundDownloadService`, `DownloadUrlResult`, `QuickDownload*`.
3. **Third-party SDKs** — ad asset downloads and ExoPlayer offline playback cache. (OnagiTube will **not** include ad SDKs unless the owner later requests them; keep playback cache as part of Media3.)

## B15. Unverified items in the reference (you must resolve them by design)

| Item | Your decision in OnagiTube |
|---|---|
| HTTP client | OkHttp, singleton, configurable timeouts, interceptors |
| Header-map owner | One class: `HeaderPolicy`, built per request (Module 7) |
| Plugin packaging | Support all three: bundled `assets/`, downloaded signed plugin, and built-in fallback (Module 3) |
| Plugin internal logic | Build a reference plugin set for permitted sources (Module 4) |
| Second map channel `e` | Implement as `metadata: Map<String,String>` for app-only tags (never sent over HTTP) |
| `BackgroundDownloadService` | Implement as a separate lightweight service for in-app playback caching (Module 14) |
| Runtime behavior | Fully test dynamically (Part D) |

---

## PART C — What to build (module by module)

### Module 1 — Project scaffolding
- Multi-module Gradle project: `:app`, `:core` (common, models, utils), `:extractor-api` (plugin contract), `:extractor-builtin` (default extractors), `:downloader` (transport engine), `:data` (Room, DataStore), `:feature-*` (UI features).
- CI workflow: lint, detekt, unit tests, instrumented tests (emulator), assemble debug/release.
- Build variants: `debug`, `release` (R8 enabled, mapping file uploaded as artifact). Product flavor optional: `full`.
- Signing config via environment variables; no secrets committed.
- `docs/` folder: `ARCHITECTURE.md`, `ASSUMPTIONS.md`, `PROGRESS.md`, `TEST_PLAN.md`, `PLUGIN_AUTHORING.md`.

### Module 2 — Entry points and URL intake (reference B2)
Implement every way a URL enters the app:
1. **Share target**: intent filter for `ACTION_SEND` (`text/plain`) and `ACTION_VIEW` for http/https.
2. **Clipboard monitor**: detect copied supported URLs when the app is foregrounded (Android 10+ restrictions respected); show a non-intrusive "Download this link?" chip. Setting to turn off.
3. **In-app browser**: WebView with address bar, back/forward, a floating download button that lights up when the current page matches a registered site; handles redirects and same-site navigation.
4. **Manual paste** field on the Home screen with validation and "Paste" button.
5. **Notification/quick tile** (optional): Quick Settings tile "Download from clipboard".
- Normalize all of these to an internal `DownloadIntent(fullUrl, referrer, mode)` carried as Intent extras **`"full_url"`** and **`"referrer"`** (keep these exact keys for compatibility with the reference).
- Implement a `LinkDownloadCoordinator` with the two entry shapes: `showPicker(activity, url, referrer, title, source)` (interactive) and `enqueueInBackground(url, context, referrer, autoStart, silent, source): Boolean` (background, with two mode switches).
- Handle: invalid URLs, unsupported sites (friendly message + "Try as direct link"), duplicate URLs already in queue (offer "Download again" / "Open existing"), offline state.

### Module 3 — Plugin architecture (reference B4)
- Define `:extractor-api` with:
  ```kotlin
  interface ExtractorPlugin {
      fun loadPlugin(classLoader: ClassLoader)
      fun get(requestJson: String): String   // returns DownloadUrlResult JSON
  }
  ```
- Host class `QuickDownloadConfigProvider` with: `loadPlugin(ClassLoader)`, `getInstance()`, `get()`, `getFormatConfigs()`, `siteCodecToJson(...)`, `getResultFromPlugin()`, and constants `METHOD_GET = "get"`, `METHOD_GET_INSTANCE = "getInstance"`, invoked reflectively.
- **Plugin loading strategies** (all three, selectable via config):
  1. Built-in (compiled into `:extractor-builtin`) — default, used as fallback.
  2. Bundled in `assets/plugins/*.apk|dex`.
  3. Downloaded over HTTPS from a configurable update endpoint, stored in app-private storage, loaded with `DexClassLoader` from a **read-only** file (Android 14+ requires read-only dex).
- **Security for downloaded plugins**: verify a detached signature (Ed25519) against a public key embedded in the app and a SHA-256 hash from a signed manifest; reject on mismatch; enforce version monotonicity (anti-rollback); sandbox failures (plugin crash must never crash the host — wrap in try/catch and a watchdog timeout).
- Plugin manifest JSON: `{ id, version, minHostVersion, sites:[{regex, formats:[...]}], entryClass, sha256, signature }`.
- Provide a **kill switch** and per-plugin enable/disable in Settings > Developer.

### Module 4 — Site matching, formats, and reference extractors (reference B3)
- Data classes:
  ```kotlin
  data class QuickDownloadFormat(val alias: String, val ext: String, val mime: String, val tag: String)
  data class QuickDownloadMap(val regex: String, val formats: Set<QuickDownloadFormat>)
  ```
- `SiteRegistry`: loads maps from plugin(s), compiles regexes once, matches in O(n) with caching; supports priority ordering and de-duplication; exposes `match(url): QuickDownloadMap?`.
- Selected `tag` is sent to the extractor in the request JSON `{ "url", "referrer", "tag" }`.
- Implement these **reference extractors** (permitted sources only), each with fixtures for tests:
  1. **Direct file** (`.mp4, .webm, .mkv, .mp3, .m4a, .ogg, .flac, .wav, .pdf, .zip, .jpg, .png`) — HEAD request to read `Content-Type`, `Content-Length`, `Accept-Ranges`, filename from `Content-Disposition`.
  2. **HLS (.m3u8) non-DRM** — parse master/media playlists, list variants (resolution/bandwidth), download segments and mux/concatenate (TS/fMP4) to MP4 where possible; reject `#EXT-X-KEY` with non-`NONE` DRM methods with a clear message.
  3. **DASH (.mpd) non-DRM** — parse AdaptationSets/Representations, download video+audio, mux; reject `ContentProtection`.
  4. **Internet Archive** (archive.org item pages).
  5. **Wikimedia Commons** media pages.
  6. **Podcast RSS** — list episodes with enclosures.
  7. **Open Graph / `<video>` / `<audio>` generic page extractor** — finds media in standard HTML tags and OG meta tags on pages that expose them openly.
- Format vocabulary: video (e.g. "1080p MP4", "720p MP4", "480p MP4", "360p MP4", "WebM"), audio ("MP3 128/192/320 kbps" via conversion, "M4A", "OGG"), image, document. Audio conversion uses an on-device converter (e.g. FFmpegKit alternative or Media3 Transformer); document the licensing of whichever library you choose in `docs/ASSUMPTIONS.md`.

### Module 5 — Asynchronous extraction result (reference B5)
- Models exactly as in B5 (`DownloadUrlResult`, `Media`, `Stream`), plus **optional** `Stream` fields: `quality`, `mime`, `sizeBytes`, `headers: Map`, `verifyType`, `digest` (this resolves the "where does the expected digest come from" gap).
- `ExtractionPoller`:
  - Sends the request, parses JSON, and loops: if `status == PENDING`, wait `waitTime` (min 500 ms, max 30 s, clamp and jitter), re-poll; if `SUCCESS`, return; if `ERROR`, surface the error.
  - Max total timeout (default 90 s) and max polls (default 60), cancellable via coroutine cancellation.
  - Exponential backoff for transient network errors, no backoff for `PENDING`.
- UI: a **Format & Quality picker** bottom sheet listing all `Stream` variants with title, cover, duration, size, quality; remembers last choice; "Best available" quick option; multi-select for playlists/batches.

### Module 6 — Transport engine (replaces the Wandoujia SDK; reference B6–B7)
Implement `:downloader` from scratch:
- `DownloadRequest` (app-level): `url`, `referrer`, `userAgent`, `headers: Map<String,String>`, `metadata: Map<String,String>` (app-only, never sent), `verifyType: NONE|MD5|SHA256`, `expectedDigest`, `contentType`, `targetDir`, `fileName`, `allowMobileData`, `priority`.
- Builder with defensive copies of maps (fixing the reference's no-copy weakness).
- `InnerDownloadRequest` (immutable) and a mapper from `DownloadRequest` mirroring the reference conversion: URL, resource type, referrer/UA conditional set when not empty, verify type and digest set only when both present, headers map forwarded.
- `HttpStreamDownloadTask`: OkHttp streaming, buffer size 64 KB, writes with `RandomAccessFile`/`FileChannel`, a CRC32 calculator updated per chunk, progress callbacks throttled to ~4/s, error classification (network, HTTP 4xx/5xx, disk full, cancelled, verification failed).
- **Ranged / block downloads**: probe with HEAD (or `Range: bytes=0-0`) for `Accept-Ranges` and length; split into blocks (default 4 blocks, min 1 MB each, configurable); compute `Range` header per block **programmatically** from segment metadata (`DownloadBlockInfo`, `DownloadSegMetaData`, `DownloadConfigInfo`); persist segment progress so downloads resume after process death; fall back to single stream when ranges unsupported; validate `ETag`/`Last-Modified` on resume (`If-Range`).
- `SingleThreadBlockDownloadTask` and `IBlockDownloadTask` abstractions with a coordinator joining blocks and verifying total length.
- Scheduler: `ExecutorService`/coroutine dispatcher pool, concurrency limit (default 3 active downloads, setting 1–5), task index (`LongSparseArray` equivalent: `ConcurrentHashMap<Long, Task>`), opaque task handle returned on enqueue, network-state listener (`ConnectivityManager.NetworkCallback`) to pause on loss / resume on regain, Wi-Fi-only option.
- Redirects, cookies (opt-in per site), gzip, TLS defaults, proxy support, timeouts (connect 15 s, read 30 s), retry with backoff (max 5), and honoring `Retry-After`.
- Storage: use Storage Access Framework / MediaStore (scoped storage) so files appear in Gallery/Music; support user-selected folder; temp files `.part` renamed atomically on success; check free space before starting; sanitize file names; handle collisions (`name (1).ext`).

### Module 7 — Header policy (reference B12)
- Single owner class `HeaderPolicy` producing the header map for each request: `User-Agent` (a clear, honest OnagiTube UA string by default; user-overridable), `Referer` (from the intake `referrer`), `Cookie` and `Authorization` only if the user explicitly provided them for that site (stored encrypted), `Range` set only by the block engine.
- Unit-test that headers are copied, not shared by reference, and that sensitive headers are never logged (redact in logs).

### Module 8 — Orchestration manager (reference B8)
`DownloadManager` (singleton, Hilt):
- Lookup: `findByKey(String): DownloadInfo?`, `findById(Long): DownloadInfo?`
- Enqueue: `enqueue(DownloadRequest): DownloadInfo`, `enqueueAsync(DownloadRequest)`
- Control: `pause(id, reason: PauseReason)`, `resume(id)`, `cancel(id)`, `retry(id)`, `delete(id, deleteFile)`, `pauseAll`, `resumeAll`
- `PauseReason`: `USER`, `NO_NETWORK`, `WIFI_ONLY`, `LOW_STORAGE`, `LOW_BATTERY`, `SYSTEM`.
- Status queries: `isRunning`, `isPaused`, `isFinished`, `isFailed`.
- Content-URI handling: `handleContentUri(Uri)` to open/play/share finished downloads and to import files via SAF.
- Policy decisions live here (what to enqueue, when to pause); the transport only executes.
- Emits a `StateFlow<List<DownloadInfo>>` and per-task `Flow<Progress>`.

### Module 9 — Foreground service (reference B9)
- `DownloadService` as a foreground service (type `dataSync`; handle Android 14/15 `onTimeout` by gracefully pausing and rescheduling via WorkManager).
- `onStartCommand` delegates to dispatcher `handleIntent(intent)` supporting actions: START, PAUSE, RESUME, CANCEL, RETRY, PAUSE_ALL, RESUME_ALL, REMOVE_FILE.
- `onBind` exposes a local binder for UI.
- Notification: channel "Downloads"; shows title, progress, speed, ETA, actions (Pause/Resume/Cancel); grouped summary; completion and failure notifications that open the file or retry.
- Request `POST_NOTIFICATIONS` (Android 13+) gracefully; service must still work if denied.
- Error entry points `onError(Throwable)` and `onFatal(Throwable)` that log and surface to UI; never crash the process.
- Second service `BackgroundDownloadService` for local-playback caching (Module 14).
- Restart-after-kill behavior (`START_STICKY` or WorkManager resume), boot-complete receiver to resume queued downloads if the user enabled it.

### Module 10 — State tracking and persistence (reference B10)
- Room database with these tables:
  - `tbl_slog_download` (keep this name and columns for fidelity): `_id` INTEGER PK AUTOINCREMENT, `type` TEXT, `source` TEXT, `file` TEXT, `downloadUrl` TEXT, `addTime` INTEGER.
  - `downloads` (rich state): id, key, url, referrer, title, coverUrl, durationMs, mime, ext, quality, totalBytes, downloadedBytes, status, pauseReason, errorCode, errorMessage, filePath/contentUri, verifyType, digest, createdAt, updatedAt, completedAt, source.
  - `segments` (per-block progress for resume).
  - `download_fail_records` (`DownloadFailRecords`): id, downloadId, errorCode, timestamp, retryCount.
  - `plugins` (installed plugin metadata), `history` (search/browse history), `settings` via DataStore.
- Broadcast/Flow contract: `EXTRA_DOWNLOAD_PROGRESS`, `EXTRA_DOWNLOAD_STATUS`, `CUSTOM_ACTION_DOWNLOAD`, `CUSTOM_ACTION_REMOVE_DOWNLOADED_FILE` for any external receivers; internally prefer Flows.
- Migrations with schema export and migration tests.
- The UI components `DownloadItemActionDialog` (per-item actions: open, share, rename, delete, redownload, copy link, details) and `DownloadingTaskFragment`-equivalent screen (active queue) read from this layer.

### Module 11 — Integrity verification (reference B11)
- Layer 1: CRC32 per stream/block during download; mismatch triggers block re-download.
- Layer 2: post-download digest check (MD5 and SHA-256 supported; the reference's "PF5" is unknown — implement as a pluggable `Verifier` interface and document that it is an extension point). Run on a background dispatcher; on failure mark `VERIFY_FAILED`, delete the partial file, and offer retry.
- Verify total length vs `Content-Length`.
- Surface verification state in the UI (a check badge).

### Module 12 — UI/UX (Compose, Material 3, light/dark, edge-to-edge)
Screens and requirements (all with loading, empty, and error states, accessibility labels, large-text support, RTL-safe layouts):
1. **Onboarding + Terms & Responsible Use** (A1.5).
2. **Home**: URL paste field, supported-sites grid (from `SiteRegistry`), recent downloads, clipboard chip.
3. **Browser**: WebView with detection overlay and download FAB.
4. **Format picker** bottom sheet (Module 5).
5. **Downloads**: tabs Active / Completed / Failed; swipe actions; multi-select; search; sort (date, size, name); storage usage header.
6. **Item details**: metadata, source URL, verification state, error log, actions.
7. **Player**: Media3 in-app player for audio/video with background audio, speed control, PiP.
8. **Settings**: download location, concurrency, Wi-Fi only, block count, default quality, naming template, clipboard monitor, notifications, theme, language, user-agent, proxy, plugin management, cache clear, export/import settings, diagnostics log export.
9. **About / Licenses / Privacy / Report a problem**.
- Localization scaffolding with at least English and Swahili strings.
- Navigation deep links for `onagitube://download?url=...`.

### Module 13 — Notifications, permissions, and lifecycle
- Runtime permission flows with rationale: notifications, (legacy) storage on API < 29, battery-optimization exemption prompt (optional, explained).
- Handle process death, configuration change, low-memory, doze, and app standby.

### Module 14 — Local playback download subsystem (reference B9, B14.2)
- `BackgroundDownloadService` + Media3 `DownloadManager`/cache for offline playback of streams the user plays in-app; independent from the main download queue; size-capped LRU cache with a Settings control and "Clear cache".

### Module 15 — Observability and diagnostics
- Structured logging (Timber) with redaction of URLs' query secrets, cookies, tokens.
- In-app **Diagnostics** screen: plugin versions, last extraction results, last 200 log lines, device info; export as a zip the user can share.
- Optional opt-in crash reporting (off by default), documented in the Privacy Policy.
- Performance instrumentation: extraction latency, throughput, retry counts (local only).

### Module 16 — Security and privacy
- No ads, no third-party trackers, no hardcoded secrets.
- Network Security Config: cleartext disabled except where the user explicitly allows per-host for direct links; certificate validation never disabled.
- Encrypt stored cookies/tokens (Jetpack Security / Keystore).
- Validate and sanitize all file names (path traversal), URLs, Intent extras (treat as untrusted), and plugin JSON (schema validation, size limits).
- Exported components minimized and permission-guarded; `FileProvider` for sharing.
- R8/ProGuard rules tested so reflection-based plugin methods `get` and `getInstance` are kept.
- Dependency vulnerability scan in CI.

---

## PART D — Testing (you must implement and run all of this)

### D1. Unit tests (JUnit5/Kotest + MockK + Turbine)
- `SiteRegistry`: regex match/no-match, priority, malformed regex handling.
- `ExtractionPoller`: PENDING -> PENDING -> SUCCESS, timeout, max polls, ERROR, cancellation, `waitTime` clamping.
- JSON parsing of `DownloadUrlResult` (all fields, unknown fields ignored, missing optional fields, malformed JSON).
- `DownloadRequest` builder: defensive map copies, conditional referrer/UA, verify type only with digest.
- `HeaderPolicy`: composition, redaction, no `Range` set by app code.
- Block planner: block boundaries for sizes (0, 1, odd sizes, huge), min block size, single-stream fallback.
- CRC calculator and digest verifiers with known vectors.
- File-name sanitizer and collision resolver.
- `DownloadManager` state machine: every transition including pause reasons, retry limits, concurrency cap.
- Plugin manifest verification: good signature, bad signature, hash mismatch, rollback attempt.

### D2. Integration tests (local MockWebServer / Robolectric)
- Full download: 200 OK stream; with ranges (206); server ignoring ranges; resume after interruption using `If-Range` and `ETag`; 301/302 redirects; 403/404/416/429 (with `Retry-After`)/500/503; slow server; connection reset mid-stream; truncated body vs `Content-Length`; gzip; large file (>2 GB) with sparse/fake stream.
- Verification pass and fail (corrupted byte, wrong digest).
- HLS and DASH fixtures: master playlists, variant selection, encrypted (DRM) rejection, segment retry.
- Room: migrations, queries, concurrent writes, process-death recovery of in-flight downloads.
- Plugin loading: built-in, asset, downloaded; crashing plugin does not crash host; timeout watchdog.

### D3. Instrumented / UI tests (Espresso + Compose testing, on API 24, 29, 33, 34/latest)
- Onboarding and terms gate blocks downloads until accepted.
- Share-sheet intake, clipboard chip, manual paste, browser detection overlay.
- Format picker selection -> enqueue -> notification progress -> completion -> open in player.
- Pause/resume/cancel/retry from UI and from the notification.
- Wi-Fi-only toggle with simulated network switch.
- Rotation, dark mode, font scale 200%, TalkBack navigation of main flows, RTL layout.
- Scoped storage: file appears in MediaStore; deletion removes file.
- Kill the process mid-download, relaunch, and confirm resume.

### D4. Non-functional tests
- **Performance**: cold start < 2 s on mid-range device; list scroll jank < 1% (Macrobenchmark); memory stable during a 1 GB download (no unbounded growth); throughput within 10% of a baseline OkHttp download.
- **Battery/background**: verify Foreground service type, Doze behavior, WorkManager resume.
- **Security**: static analysis (detekt, Android Lint security checks), dependency scan, test for path traversal file names, malicious plugin JSON, oversized responses, redirect to `file://` or `content://` (must be rejected), cleartext policy.
- **Monkey/fuzz**: Intent extras fuzzing (`full_url`, `referrer`), regex fuzzing for ReDoS (enforce match timeouts).
- **Compatibility**: API 24–latest; phone and tablet; low-RAM device profile.
- **Release build check**: run the full instrumented suite against the R8-minified release build to confirm reflection keep-rules work.

### D5. Test deliverables
- `docs/TEST_PLAN.md` listing every test, the requirement ID it covers (B1–B15 and Modules 1–16), and its result.
- Coverage report: target >= 80% line coverage on `:downloader`, `:extractor-api`, `:core`; >= 60% overall.
- A **requirements traceability matrix** (CSV or table): each Module/B-item -> implementation files -> tests -> status.
- Record at least one screen recording or screenshot set per main flow from the emulator.

---

## PART E — Definition of done and deliverables

The work is complete only when **all** of the following are true:
1. `./gradlew clean build` passes; lint and detekt pass with zero errors.
2. All unit, integration, and instrumented tests pass; failures, if any, are listed with root cause and are not hidden or skipped silently.
3. A signed (debug-key acceptable) `OnagiTube-release.apk` is produced and installs on an emulator; main flows verified end-to-end against a local test server and at least three permitted real-world sources.
4. Every item in Part B (B1–B15) and every Module (1–16) is implemented or explicitly justified in `docs/ASSUMPTIONS.md` with a fallback.
5. The reference gaps in B15 are resolved as specified.
6. `docs/` is complete: ARCHITECTURE (with diagrams: component diagram, download sequence diagram, state machine), PLUGIN_AUTHORING, TEST_PLAN, PROGRESS, ASSUMPTIONS.
7. README covers: setup, build, run, test, adding a site/extractor, plugin signing, troubleshooting.
8. Final report to the owner includes: what was built, what was tested (with real command output summaries), known limitations, legal/compliance notes (A1), and a prioritized backlog.

### Final instruction
Begin by producing `docs/ARCHITECTURE.md` and `docs/PROGRESS.md`, then execute Modules 1 -> 16 in order, testing as you go (Part D), and finish with the Part E report. Do not stop to ask for confirmation unless a requirement conflicts with the legal guardrails in A1; in that case, follow A1 and record the conflict.
