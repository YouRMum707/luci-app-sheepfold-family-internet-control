# Messenger Bot

Sheepfold should support messenger control through adapters.

Planned adapters:

- `telegram`
- `vk`
- `max` experimental

The bot must use the router API safely, restrict access to approved users, and require confirmation for destructive actions such as reboot, update, and global block changes.

On OpenWRT the preferred Telegram implementation is a local `procd` service that uses outbound HTTPS long polling (`getUpdates`). This avoids exposing the router through a public webhook.

VK should use the same internal adapter interface as Telegram. It is the default first-run messenger choice and is more justified as a stable adapter than MAX for the current plan because VK's bot ecosystem is more established.

The installed router config should keep messenger `active` set to `none` until the parent/admin enters credentials and binds at least one approved administrator. The default setup choice should be VK, not automatic activation.

MAX may remain as an experimental adapter, but it must not block the first stable Telegram/VK implementation.
