# Security Model

## Principles

- Do not store the router root password in the Android app.
- Use API tokens or session-based authentication.
- Restrict management API access to the local network by default.
- Sheepfold is a family self-hosted tool by default; do not require a developer-operated cloud service.
- The Android app is for parent/admin devices only, not for hidden installation on children's phones.
- Validate MAC addresses, IP addresses, hostnames, and domains.
- Avoid shell injection.
- Avoid duplicate nftables rules.
- Restore rules after `fw4 restart` and router reboot.
- Target `firewall4` / `nftables`; do not add legacy `firewall3` / `iptables` compatibility unless explicitly required later.

## Android App Authentication

During first setup, the Android app should ask which local app-lock method to use.

Recommended default: password or PIN.

Fingerprint and face unlock may be available, but should not be recommended as the safest default for a parental-control app. The UI should explain this briefly:

```text
Password or PIN is recommended. Fingerprint or face unlock can be less safe for parental-control apps: a child may try to unlock the app while the parent is asleep.
```

## List Conflicts

The same device must not be present in both allowlist and blocklist.

This rule must be enforced in:

- backend validation;
- LuCI forms;
- Android app;
- bot commands;
- import validation.

## Administrative Actions

The following actions must require confirmation:

- router reboot;
- application update;
- global internet block;
- settings import;
- reset settings.

## Logging

Logging must be configurable.

Allowed retention values:

```text
6m
3m
1m
14d
7d
3d
1d
12h
3h
1h
off
```

Default:

```text
3d
```

Use a size cap as well as a time cap. Default size cap:

```text
1024 KB
```

Do not write secrets, bot tokens, API keys, passwords, session cookies, full messenger conversations, full AI prompts, full browsing history, raw DNS query history, banking data, medical data, or exact private details about children.

Mask sensitive values in exported logs by default:

- partially mask MAC addresses;
- mask the last IP octet in exports;
- partially mask messenger user IDs and chat IDs;
- always replace tokens, API keys, passwords, and session IDs with `[secret]`.

LuCI and Android must include a `Clear log` action with confirmation. Log export should default to masked export.
