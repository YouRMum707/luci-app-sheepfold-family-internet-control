# Security Model

## Principles

- Do not store the router root password in the Android app.
- Use API tokens or session-based authentication.
- Restrict management API access to the local network by default.
- Validate MAC addresses, IP addresses, hostnames, and domains.
- Avoid shell injection.
- Avoid duplicate nftables rules.
- Restore rules after `fw4 restart` and router reboot.
- Target `firewall4` / `nftables`; do not add legacy `firewall3` / `iptables` compatibility unless explicitly required later.

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
