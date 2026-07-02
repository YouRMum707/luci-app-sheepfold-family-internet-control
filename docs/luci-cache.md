# LuCI Browser Cache And Asset Versioning

Sheepfold should avoid asking users to clear the browser cache after every LuCI update.

## Rule

Keep one project-level LuCI asset version and append it to every Sheepfold JS/CSS/static asset URL:

```text
/luci-static/resources/sheepfold/app.css?v=0.1.0-1
/luci-static/resources/sheepfold/client-table.js?v=0.1.0-1
```

The canonical version should come from the OpenWRT package version:

```make
PKG_VERSION:=0.1.0
PKG_RELEASE:=1
SHEEPFOLD_UI_ASSET_VERSION:=$(PKG_VERSION)-$(PKG_RELEASE)
```

The generated/runtime value should be:

```text
ui_asset_version = PKG_VERSION-PKG_RELEASE
```

The default UCI config may expose the value as:

```text
option ui_asset_version '0.1.0-1'
```

The package `postinst` should write `SHEEPFOLD_UI_ASSET_VERSION` into UCI during install/update. Future implementation may generate a small LuCI config module during package build instead of relying only on UCI defaults. The important rule is that developers change the version in one place for a release and all LuCI asset URLs receive the new cache-busting suffix.

## Requirements

- Do not hardcode different asset versions in individual LuCI files.
- Use the same `ui_asset_version` for all Sheepfold LuCI CSS, JS, images, fonts, and extra static resources.
- Bump the version when LuCI frontend files change.
- The update script should restart/reload the Sheepfold service after package update and may show a short cache-clear hint only as a fallback.
- Keep the Podkop browser-cache clearing link in README/docs as a troubleshooting link, not as the normal update path.

## Limitation

The main LuCI view loader may still be affected by LuCI/OpenWRT browser cache behavior. Cache-busting our own linked assets reduces the common problem, but the troubleshooting documentation should still mention manual cache clearing for rare stale-view cases.
