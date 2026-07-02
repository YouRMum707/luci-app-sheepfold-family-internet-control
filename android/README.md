# Android App

The Android companion app is planned under the name **Овчарня**.

- Android package: `app.sheepfold.android`
- Minimum Android version: Android 9.0 Pie
- Minimum SDK: API 28
- Target SDK: latest stable Android SDK
- Suggested stack: Kotlin, Jetpack Compose, Android Keystore for token storage
- Planned widgets:
  - Block internet
  - Unblock internet
  - Grant +30 minutes

The app should mirror the core LuCI workflows and include the parent AI assistant tab.

## App Lock

On first setup, the Android app should ask how to protect access to the app.

Recommended default: password or PIN.

Biometric unlock by fingerprint or face can be offered, but should not be recommended as the safest option. Short warning text:

```text
Password or PIN is recommended. Fingerprint or face unlock can be less safe for parental-control apps: a child may try to unlock the app while the parent is asleep.
```

Android versions older than 9.0 are intentionally out of scope.
