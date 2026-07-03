# Integrations

## LuCI Setting

Add a LuCI setting:

```text
Use together with
```

Russian label:

```text
Использование совместно с
```

Suggested values:

| Value | UI label | Meaning |
| --- | --- | --- |
| `none` | None | Sheepfold works alone. |
| `adguard` | AdGuard Home | Sheepfold allows/blocks devices first, then DNS filtering goes through AdGuard Home. |
| `podkop` | Podkop | Sheepfold allows/blocks devices first, then compatible traffic routing continues through Podkop. |
| `adguard_podkop` | AdGuard Home + Podkop | Preferred advanced chain: Sheepfold -> AdGuard Home -> Podkop. |

Do not use a simple mutually exclusive `none/adguard/podkop` model only. AdGuard Home and Podkop can be used together, and this is one of the target scenarios.

The setting should be stored as:

```text
option integration_mode 'none'
```

Allowed values:

```text
none
adguard
podkop
adguard_podkop
```

## Configuration UX

When the user selects an integration mode, LuCI should show integration-specific notes before applying changes.

Automatic router changes are allowed only after explicit confirmation.

This setting is needed. It is not a cosmetic label and it must not simply mean "AdGuard/Podkop is installed". It controls the compatibility plan Sheepfold should use when preparing firewall, DNS, domain allowlist, diagnostics, and troubleshooting notes.

Without this setting, Sheepfold cannot safely decide whether to:

- leave DNS/routing alone;
- expect AdGuard Home to be the DNS filtering layer after Sheepfold's device decision;
- avoid Podkop-managed routing/Dnsmasq/nftables/sing-box state;
- show the correct diagnostics when domain allowlisting or emergency-useful access does not work as expected.

Suggested confirmation copy:

```text
Sheepfold can adjust router settings for this integration. Review the planned changes before applying them. A backup/export should be created first.
```

Russian:

```text
Sheepfold может изменить настройки роутера для этой интеграции. Перед применением проверьте список изменений. Сначала рекомендуется сделать экспорт/резервную копию настроек.
```

## Automatic Setup

Automatic setup should be conservative.

The installer must check the router before applying Sheepfold defaults:

- detect whether AdGuard Home is installed;
- detect whether Podkop is installed;
- set the recommended Sheepfold `integration_mode`;
- set only Sheepfold-owned integration flags automatically;
- show the detected state in the installer output and later in LuCI.

Recommended installation defaults:

| Detected state | Recommended `integration_mode` |
| --- | --- |
| Neither AdGuard Home nor Podkop | `none` |
| AdGuard Home only | `adguard` |
| Podkop only | `podkop` |
| AdGuard Home and Podkop | `adguard_podkop` |

Allowed automatic actions after confirmation:

- check whether AdGuard Home is reachable;
- check whether Podkop is installed/enabled;
- detect Dnsmasq and firewall state;
- create a Sheepfold config export before changes;
- enable integration flags in Sheepfold config;
- show commands/changes that still require manual action.

Suggested apply-time skeleton:

1. Read selected `integration_mode`.
2. Run detection checks for AdGuard Home, Podkop, Dnsmasq, firewall4/nftables, and Sheepfold-owned chains/sets.
3. Show a planned-changes preview in LuCI.
4. Create/export a Sheepfold backup before applying.
5. Apply only Sheepfold-owned UCI options and Sheepfold-owned firewall/nftables state automatically.
6. Mark checks that need manual action instead of silently changing third-party configs.
7. Log the administrator, selected mode, checks, planned changes, result, and warnings with secrets masked.

Avoid automatic destructive changes:

- do not overwrite AdGuard Home config blindly;
- do not overwrite Podkop config blindly;
- do not reset firewall rules outside Sheepfold-owned chains/sets;
- do not restart major services without warning.

## AdGuard Home

Sheepfold should not replace AdGuard Home. The intended chain is:

1. Sheepfold device access decision.
2. AdGuard Home DNS filtering.
3. Podkop routing.

Possible integration points:

- discover clients from AdGuard Home;
- map AdGuard clients to Sheepfold devices by IP/MAC/name;
- expose integration status in LuCI;
- avoid taking over DNS rules in a way that breaks AdGuard Home.

LuCI should display AdGuard Home status when this mode is selected or detected:

- installed/not detected;
- service running/stopped when this can be checked locally;
- API reachable/not reachable;
- version if the API returns it;
- last check time;
- whether credentials/API endpoint are configured in Sheepfold.

Use the local AdGuard Home API only when credentials are configured by the parent/admin. Do not store or display the full password/token in logs or exports.

LuCI notes for this mode:

- Sheepfold should block/allow devices before DNS filtering.
- AdGuard Home remains responsible for DNS filtering.
- Emergency-useful sites may need special care, because DNS filtering and domain allowlisting can overlap.
- If AdGuard Home is not reachable, show a warning and do not enable automatic mode silently.

Useful external reference:

- [Podkop: connecting AdGuard Home and Podkop](https://podkop.net/docs/adguard/)

## Podkop

Podkop should remain responsible for routing selected traffic through its configured path.

Sheepfold must avoid conflicting with Podkop-managed Dnsmasq, nftables, sing-box, and routing state.

LuCI notes for this mode:

- Podkop remains responsible for traffic routing after Sheepfold allows the device.
- Sheepfold must avoid modifying Podkop-managed Dnsmasq, nftables, sing-box, or routing state.
- If Podkop is installed but disabled, show a warning.
- If Podkop is not detected, do not enable Podkop compatibility silently.

Podkop currently should be treated as "local package/service/config detection" unless a stable Sheepfold-facing API appears later. LuCI status should therefore be conservative:

- package/config detected or not detected;
- service enabled/running if available through init/service state;
- compatibility mode selected;
- warning when Podkop is selected but not detected;
- warning that Sheepfold must not overwrite Podkop-managed routing, Dnsmasq, nftables, or sing-box state.

Useful external references:

- [Podkop documentation](https://github.com/itdoginfo/podkop)
- [Podkop: connecting AdGuard Home and Podkop](https://podkop.net/docs/adguard/)
- [Podkop: clearing browser cache after LuCI updates](https://podkop.net/docs/clear-browser-cache/)

## Router Selection

Sheepfold targets modern OpenWRT routers with `firewall4` / `nftables`.

For choosing compatible hardware, link users to:

- [OpenWRT router comparison](https://hattabbi4.github.io/openwrt-router-compare/)
