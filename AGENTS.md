# Agent Guidelines

These rules apply to the whole repository.

## Project Naming

- Use `Sheepfold` as the main project and product name in English and Russian text.
- Use `Овчарня` only when referring to the Android app name or the Russian LuCI display name.
- Do not use `Овчарня` as the generic Russian name for the whole project outside direct app/interface naming.

Correct examples:

- `Sheepfold — система семейного управления доступом...`
- `Android-приложение: Овчарня`
- `LuCI RU: Овчарня : контроль доступа в интернет для семьи`

Avoid:

- `Овчарня — система...`
- `Если Овчарня окажется полезной...`

## User-Facing Wording

- Prefer clear router-based wording over internal-only LuCI wording.
- In Russian, write `через OpenWRT-роутер и его веб-интерфейс LuCI` instead of only `через LuCI`.
- Keep README files approachable for non-developers.
- Keep user-facing strings localizable. Do not hardcode menu labels, validation messages, or bot replies when a localization resource should be used.

## README Layout

- Keep installation and update instructions near the top of both `README.md` and `README.ru.md`, before long product explanations.
- Keep the English and Russian README files structurally similar where practical.
- When changing installation or update commands, update both README files and `docs/github-install-setup.md` if relevant.

## Emergency-Useful Sites

- The user-facing feature name is:
  - RU: `Доступ к аварийно-полезным сайтам`
  - EN: `Access to emergency-useful sites`
- Do not call this feature `белый список доменов` in user-facing Russian UI copy.
- Use one editable domain list, not built-in presets. AdGuard Home already has filtering presets; Sheepfold should not duplicate them.
- Every automatically suggested site entry must include:
  - domain;
  - user-visible display name;
  - short explanation of why the site may be needed;
  - optional warning;
  - source, for example `starter`, `manual`, `imported`, or `integration`.
- Emergency-useful sites are for restricted access: enough for necessary services, not enough for normal entertainment browsing.

## Yandex Domains

- Do not add broad `yandex.ru` to any default, starter, auto-generated, auto-imported, or "safe minimum" emergency-useful sites list.
- Do not silently include broad Yandex domains when creating examples, onboarding defaults, first-run suggestions, import templates, generated configs, tests, fixtures, or screenshots.
- If Yandex search is needed by default, prefer `ya.ru`.
- If maps are needed by default, prefer `2gis.ru`.
- Add broad Yandex domains only when the parent/admin explicitly adds them manually or selects an advanced option with a clear warning.
- Always explain the reason: broad Yandex domains can open much more than maps or search, including video, music, games, feeds, entertainment pages, and other Yandex services. Yandex Maps may require shared Yandex/static domains, so narrow allowance can be difficult.

## Messaging

- Treat Telegram as a notification channel: "what happened?"
- Treat MAX as a two-way chat/control channel: "what should we do now?"
- Messenger integrations must use the same Sheepfold API as LuCI and Android.
- Administrative bot actions such as reboot, update, import, global block, and list changes must require explicit confirmation.

## Localization

- Russian is the primary product wording source.
- English is the required fallback language.
- Translation files are small; keep them in the repository/package by default instead of downloading them separately.
- Planned generated UI languages include Spanish, German, French, Portuguese (Brazil), Italian, Polish, Turkish, Ukrainian, Chinese Simplified, Japanese, Korean, Arabic, Hindi, Indonesian, and Vietnamese.

## Platform Scope

- Target modern OpenWRT with `firewall4` / `nftables`.
- Do not add legacy `firewall3` / `iptables` support unless explicitly requested later.
- Target Android 9.0 Pie / API 28 and newer.

## Repository Hygiene

- Keep `README.md` in English.
- Keep `README.ru.md` in Russian.
- Keep shell scripts and OpenWRT package files LF-only.
- Do not commit secrets, tokens, router passwords, or local environment files.
