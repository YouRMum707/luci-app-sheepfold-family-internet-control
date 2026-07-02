# Integrations

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

## Podkop

Podkop should remain responsible for routing selected traffic through its configured path.

Sheepfold must avoid conflicting with Podkop-managed Dnsmasq, nftables, sing-box, and routing state.
