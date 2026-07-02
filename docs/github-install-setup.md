# GitHub Install Setup

Repository:

```text
https://github.com/kva4991/luci-app-sheepfold-family-internet-control
```

Public install command:

```sh
wget -O /tmp/sheepfold-install.sh https://raw.githubusercontent.com/kva4991/luci-app-sheepfold-family-internet-control/main/install.sh
sh /tmp/sheepfold-install.sh
```

## Release Plan

1. Build `.ipk` packages in GitHub Actions.
2. Publish packages to GitHub Releases.
3. Teach `install.sh` to detect OpenWRT architecture.
4. Download the matching `.ipk`.
5. Install dependencies through `opkg`.
6. Install Sheepfold through `opkg install`.
7. Enable and start the service.
8. Restart `rpcd`, `uhttpd`, and `firewall` when needed.

## Update Plan

The LuCI and Android "Update app" buttons should:

1. Check latest GitHub Release.
2. Compare current and latest versions.
3. Create a settings backup.
4. Download the matching `.ipk`.
5. Install it.
6. Restart required services.
7. Report success or failure.
