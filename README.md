# LOS — starter build

A lightweight Android home-screen launcher prototype for the Samsung Galaxy J2 Pro (Android 6.0.1 / API 23).

## Current prototype
- HOME launcher intent, so Android can offer LOS as the default home app.
- WebView-based home screen with live clock/date and dark gradient wallpaper.
- Reads installed launcher apps and their icons through a small JavaScript bridge.
- App drawer with search and launch support.
- Responsive layout intended for small phone screens.

## Build in the cloud (GitHub Actions)
1. Create a private GitHub repository and upload the contents of this folder.
2. In GitHub, open **Actions** and enable workflows if prompted.
3. Run the **Build LOS APK** workflow from the Actions tab.
4. Download the `LOS-debug-apk` artifact from the completed workflow.
5. Transfer the APK to the LOS test phone and install it. Android may ask you to allow installation from that file app.
6. Press Home and select LOS. Keep the Samsung launcher available until testing is complete.

## Local build
Requires Android Studio, JDK 17, Android SDK platform 35, and Gradle 8.7:
`gradle assembleDebug`

APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## Important
This is an early prototype, not a finished launcher. Test app launching, screen rotation, memory use, and Home-button behavior on the actual device before making it the default. The original Samsung launcher is not removed or modified.
