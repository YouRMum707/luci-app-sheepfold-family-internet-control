# Messaging And Notifications

Sheepfold should support two different messaging use cases:

1. Notifications to parents.
2. Interactive chat control.

## Telegram Notifications

Telegram is a good channel for push-style parent notifications.

Planned events:

- global block enabled;
- global block disabled;
- new unknown device detected;
- device added to allowlist;
- device added to blocklist;
- temporary access granted;
- schedule rule applied;
- import/export completed;
- router reboot requested;
- application update completed or failed;
- AdGuard Home integration warning;
- Podkop integration warning;
- firewall/rpcd/service error;
- child access request, if this workflow is added later.

Requirements:

- Telegram notifications must be optional.
- Store bot token securely.
- Allow configuring one or more approved chat IDs.
- Do not send router passwords, API tokens, or sensitive network secrets.
- Respect the logging setting: if logs are disabled, live notifications may still be sent, but Sheepfold should not create local event history unless explicitly enabled.

## MAX Two-Way Chat

MAX should be treated as an interactive messenger channel, not only a notification sink.

Planned capabilities:

- show current status;
- show all devices;
- search devices by name, IP, or MAC;
- enable/disable global blocking with confirmation;
- grant temporary access;
- add/remove devices from allowlist or blocklist;
- approve or reject a child access request;
- ask for confirmation before reboot/update/import;
- accept short natural Russian commands.

Architecture:

- implement MAX as a separate adapter;
- keep messenger-specific code outside OpenWRT firewall logic;
- route all actions through the same Sheepfold API used by LuCI and Android;
- keep command permissions explicit per approved user.

## UX Principle

Telegram answers: "What happened?"

MAX answers: "What should we do now?"
