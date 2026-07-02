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

Useful external reference:

- [Podkop: connecting AdGuard Home and Podkop](https://podkop.net/docs/adguard/)

## Podkop

Podkop should remain responsible for routing selected traffic through its configured path.

Sheepfold must avoid conflicting with Podkop-managed Dnsmasq, nftables, sing-box, and routing state.

Useful external references:

- [Podkop documentation](https://github.com/itdoginfo/podkop)
- [Podkop: connecting AdGuard Home and Podkop](https://podkop.net/docs/adguard/)
- [Podkop: clearing browser cache after LuCI updates](https://podkop.net/docs/clear-browser-cache/)

## Router Selection

Sheepfold targets modern OpenWRT routers with `firewall4` / `nftables`.

For choosing compatible hardware, link users to:

- [OpenWRT router comparison](https://hattabbi4.github.io/openwrt-router-compare/)
