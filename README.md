# Call History

- App label: Call History
- Package: `com.callhistory.app`
- Kotlin + Jetpack Compose
- Minimum Android: 11 (API 30)
- GitHub Actions workflow: `.github/workflows/build-apk.yml`
- AdMob: Google's official sample test application and banner IDs.

## Build
Upload the complete folder contents to the root of a GitHub repository. The workflow installs Gradle 8.9 and Android SDK 35, then creates a debug APK. Download it from the Actions run's Artifacts section.

## First launch
Open the app and tap **Enable call reporting**, then accept Android's Call Screening role request. The service is coded to allow calls; it does not block, reject, or silence them.

## Important testing notes
Android's `ACTION_POST_CALL` behavior depends on the Android system and selected call-screening role. Test incoming and outgoing calls on the actual target phone. The current starter displays the number; contact-name lookup and exact matching of every field from the old screenshot still need device validation/refinement. The 5-second display is visual and does not close the popup. Daily totals start from calls received after setup, not imported historic call logs.

Keep test ads during development. Replace the sample ad unit with your own AdMob unit only for production.
