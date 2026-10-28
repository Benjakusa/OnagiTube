# OnagiTube — Handoff (verified state)

Last verified: 2026-10-28. Build: GREEN. APK: built + installs. Runtime: CRASHES ON LAUNCH (open bug).

## Environment
- JDK 17, Android SDK at `/home/benjakusa/Android/Sdk` (platform 34, build-tools 34.0.0)
- Gradle 8.5 wrapper (`./gradlew`), AGP 8.2.2, Kotlin 1.9.22, KSP 1.9.22-1.0.17
- `gradle.properties`: `android.useAndroidX=true`, `-Xmx2048m`, parallel + caching on
- Catalog: `gradle/libs.versions.toml` (valid TOML)

## Modules
- `:app` — android.application, namespace `com.onagitube.app`, applicationId `com.onagitube`, minSdk 26, compileSdk/targetSdk 34
- `:core` — android.library, namespace `com.onagitube.core` (uses `android.app.Service`, `android.content.Intent`)
- `:data` — android.library + Room + KSP, namespace `com.onagitube.data`
- `:downloader`, `:extractor-api`, `:extractor-builtin` — kotlin.jvm

## Open issue: crash on launch
App installs but closes immediately on tap. No crash log captured yet.

### Capture the crash
```bash
export ANDROID_HOME=/home/benjakusa/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
adb devices                       # confirm device authorized
adb logcat -c
adb shell am start -n com.onagitube/com.onagitube.app.ui.MainActivity
sleep 2
adb logcat -d | grep -A 30 "FATAL EXCEPTION"
```
If grep returns nothing: `adb logcat -d | grep -iE "com.onagitube|AndroidRuntime" | tail -60`

### Prime suspects (ranked)
1. **Manifest activity names** — check `cat app/src/main/AndroidManifest.xml`. If `android:name=".ui.MainActivity"`, launcher resolves against the (removed) manifest package → fails. Fix: fully-qualify to `com.onagitube.app.ui.MainActivity` (and same for ShareTargetActivity, InAppBrowserActivity).
2. **Theme resolution** — `@style/Theme.OnagiTube` must exist. `themes.xml` should have both `Theme.OnagiTube` and `Theme.OnagiTube.NoActionBar` (15 lines, closes with `</resources>`). Verify with `xmllint --noout app/src/main/res/values/themes.xml`.
3. **ConstraintLayout / tint** — `activity_main.xml` uses `app:tint`; the root must declare `xmlns:app="http://schemas.android.com/apk/res-auto"`.

## Rebuild + reinstall
```bash
cd /home/benjakusa/onagiTube
export ANDROID_HOME=/home/benjakusa/Android/Sdk
export ANDROID_SDK_ROOT=$ANDROID_HOME
./gradlew :app:assembleDebug --console=plain
$ANDROID_HOME/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Known pitfalls
- Heredoc `cat << EOF` truncates on this terminal when paste >30 lines. Use `printf "%s\n" "l1" "l2" ...` for long files.
- `$ANDROID_HOME` is not persisted between terminal sessions. Re-export per session.
- `build/` dirs were once committed to git; untracked via `git rm --cached`. Verify: `git ls-files | grep -c /build/` → `0`.

## Not yet implemented (do not assume these work)
- Top tabs (Search/YouTube/Music/More) and bottom nav (Download/Play/Settings) are cosmetic only — no fragment switching.
- `ExtractionPoller.parseResult` is a stub returning `SUCCESS`.
- `DownloadManager` is in-memory only; no actual HTTP download logic.
- Plugin loading via reflection is stubbed.
