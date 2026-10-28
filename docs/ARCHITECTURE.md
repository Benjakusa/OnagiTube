# OnagiTube Architecture

## Component Diagram

```mermaid
graph TD
    UI[UI Layer - Compose]
    VM[ViewModel Layer - MVVM]
    DM[Domain Layer - Use Cases]
    DA[Data Layer - Repositories, DAOs]
    DB[Room Database]
    DS[DataStore - Settings]
    NF[Network Layer - OkHttp + Serialization]
    EX[Extraction Plugins]
    SF[Foreground Service - Downloads]
    WM[WorkManager - Retries/Scheduled]
    NP[Notification Player - Media3/ExoPlayer]
    PL[Plugin Manager - Load/Unload]

    UI --> VM
    VM --> DM
    DM --> DA
    DA --> DB
    DA --> DS
    NF -->|HTTP Requests| EX
    NF -->|API Calls| PL
    EX -->|Parsed Media Data| NF
    PL -->|Extractor Instances| EX
    SF -->|Foreground Service Control| WM
    WM -->|Task Scheduling| NF
    NP -->|Media Playback| Media3[ExoPlayer]
    SF -->|Status Updates| NP
```

## Download Sequence Diagram

```mermaid
sequenceDiagram
    participant U as User
    participant UI as UI (Compose)
    participant VM as ViewModel
    participant RE as Repository
    participant DL as Downloader
    participant SV as Service
    participant NO as Notification
    participant PL as Plugin Loader
    participant EX as Extractor

    U->>UI: Paste URL / Share
    UI->>VM: onUrlPasted(url)
    VM->>RE: enqueueDownload(url)
    RE->>PL: loadExtractor(url)
    PL-->>RE: Extractor instance
    RE->>DL: addTask(extractor, url)
    DL->>EX: extractMetadata(url)
    EX-->>DL: format options
    UI->>DL: showFormatPicker(formats)
    UI->>DL: selectFormat(format)
    DL->>EX: downloadMedia(format)
    EX-->>DL: output stream
    DL->>SV: startForegroundService()
    SV->>NO: showProgress()
    Note over EX,DL: Download in progress
    SV->>NO: updateProgress()
    SV->>NO: completeDownload()
    NO->>UI: openInPlayer or dismiss
    UI->>VM: onDownloadComplete()
    VM->>RE: markCompleted()
    RE->>DB: saveRecord()
    DB-->>RE: success
```

## State Machine (Download Status)

```mermaid
stateDiagram-v2
    [*] : Initial
    Initial :enqueued: -> Enqueued
    Enqueued :startService: -> Downloading
    Downloading :pause: -> Paused
    Downloading :complete: -> Completed
    Downloading :error: -> Error
    Downloading :cancel: -> Cancelled
    Paused :resume: -> Downloading
    Paused :cancel: -> Cancelled
    Error :retry: -> Downloading
    Cancelled :re-enqueue: -> Enqueued
    Completed :* : -> [*]
```

## Key Architecture Notes

- **MVVM + Clean Layers**: `ui` (Compose), `domain` (Use Cases), `data` (Repositories, DAOs, Room, DataStore), `core` (DI via Hilt, Coroutines/Flow)
- **Extraction Separation**: Plugins loaded at runtime; crashing plugin isolated host
- **Transport Layer**: OkHttp 4/5 + kotlinx.serialization (self-implemented, no Wandoujia SDK)
- **Background Work**: Foreground Service for active downloads, WorkManager for retries/scheduling
- **Media Playback**: Media3 (ExoPlayer) for in-app preview and completion handling
- **Image Loading**: Coil
- **Persistence**: Room (SQLite) for download history, DataStore for user settings

## Permitted Sources (B4)

Direct file URLs, user-owned or Creative Commons/public-domain content, podcasts/RSS enclosures, open HLS/DASH streams (non-DRM).