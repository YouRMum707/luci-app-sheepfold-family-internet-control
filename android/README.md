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

## Connectivity

The Android app is for parent/admin devices only.

Default connection model:

- local network connection to the router when the parent is at home;
- Telegram or MAX bot for remote short commands and notifications when the parent is away.

Without VPN or a developer-operated cloud service, the Android app should not promise full remote router management outside the local network.

## First-Run Agreement

Before the first setup continues, the Android app must show a link to the full user agreement and require this checkbox:

```text
Я принимаю пользовательское соглашение и даю согласие на обработку персональных и технических данных, необходимых для работы Sheepfold.
```

Full agreement:

```text
https://github.com/kva4991/luci-app-sheepfold-family-internet-control/blob/main/docs/user-agreement.ru.md
```

## App Lock

On first setup, the Android app should ask how to protect access to the app.

Recommended default: password or PIN.

Biometric unlock by fingerprint or face can be offered, but should not be recommended as the safest option. Short warning text:

```text
Password or PIN is recommended. Fingerprint or face unlock can be less safe for parental-control apps: a child may try to unlock the app while the parent is asleep.
```

Android versions older than 9.0 are intentionally out of scope.
