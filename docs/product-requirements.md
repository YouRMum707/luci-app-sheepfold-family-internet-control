# Product Requirements

## Final Names

- GitHub repository: `luci-app-sheepfold-family-internet-control`
- OpenWRT package: `luci-app-sheepfold-family-internet-control`
- LuCI EN: `Sheepfold Family Internet Control`
- LuCI RU: `Овчарня : контроль доступа в интернет для семьи`
- Android app: `Овчарня`
- Android package: `app.sheepfold.android`

## Core Features

- Manage family internet access through an OpenWRT router and its LuCI web interface.
- Android companion app.
- VK/MAX messenger bot.
- Telegram notifications for parent alerts.
- MAX two-way chat for interactive parent communication.
- Device allowlist.
- Device blocklist.
- Automatically discovered devices.
- Search by MAC, current IP, hostname, and custom name.
- DHCP static lease synchronization.
- Schedules.
- Temporary access tokens.
- Access to emergency-useful sites for restricted mode.
- Import/export of all settings and known clients.
- Optional logging.
- App update and router reboot controls with confirmation.

## Localization

Sheepfold must support Russian and English UI text and should be ready for generated translations into popular languages.

Requirements:

- keep UI strings in localization resources, not hardcoded directly in LuCI JS or Android code;
- provide Russian as the primary wording source;
- keep English as the required fallback language;
- generate and maintain translations for all LuCI, Android, bot, and documentation-facing menu labels;
- planned generated languages: Spanish, German, French, Portuguese (Brazil), Italian, Polish, Turkish, Ukrainian, Chinese Simplified, Japanese, Korean, Arabic, Hindi, Indonesian, and Vietnamese;
- keep terminology consistent:
  - `Sheepfold` as the project name;
  - `Овчарня` only for the Android app name and Russian LuCI display name;
  - `Доступ к аварийно-полезным сайтам` / `Access to emergency-useful sites` for the restricted-domain feature.

## Android Scope

The Android companion app **Овчарня** should support Android 9.0 Pie / API 28 and newer.

Older Android versions are intentionally out of scope.

Recommended Android baseline:

- minimum SDK: API 28;
- target SDK: latest stable Android SDK;
- implementation language: Kotlin;
- UI: Jetpack Compose;
- token storage: Android Keystore.

## Target OpenWRT Scope

The project should target modern OpenWRT installations that use `firewall4` and `nftables`.

There is no need to support old OpenWRT versions based on `firewall3` / `iptables`. The expected routers are relatively modern home routers, for example devices in the class of Xiaomi Mi Router AX3000T.

## Device Rules

Blocklisted devices are always blocked. Allowlisted devices are never blocked by global blocking or schedules. The backend and UI must prevent the same MAC address from being present in both lists.

Temporary access must never bypass the blocklist.

## Router Interface Access

The application should include security settings for local router access:

- blocklisted devices cannot access the OpenWRT router LuCI interface, SSH, or the Sheepfold local API;
- globally blocked devices may access the router only if `allow_router_for_blocked` is enabled;
- emergency-useful sites mode can optionally allow selected public domains for blocked devices.

## Integrations

AdGuard Home should remain responsible for DNS-level filtering when enabled.

Podkop should remain responsible for routing after Sheepfold and AdGuard Home have allowed the traffic.

## Helpful External Links

- Podkop and AdGuard Home setup: https://podkop.net/docs/adguard/
- Clearing browser cache after LuCI updates: https://podkop.net/docs/clear-browser-cache/
- OpenWRT router comparison: https://hattabbi4.github.io/openwrt-router-compare/

## Messaging

- Telegram should support optional parent notifications for important events and errors.
- MAX should support two-way chat: status, device search, temporary access, approvals, and confirmed administrative actions.
- Messenger integrations must go through the same Sheepfold API used by LuCI and Android.
