#!/bin/sh
set -eu

OWNER="kva4991"
REPO="luci-app-sheepfold-family-internet-control"
PACKAGE="luci-app-sheepfold-family-internet-control"

echo "Sheepfold Family Internet Control installer"
echo "Repository: ${OWNER}/${REPO}"

if [ ! -r /etc/openwrt_release ]; then
    echo "ERROR: This installer must be run on OpenWRT." >&2
    exit 1
fi

. /etc/openwrt_release

echo "Detected OpenWRT: ${DISTRIB_DESCRIPTION:-unknown}"
echo "Package: ${PACKAGE}"

if ! command -v opkg >/dev/null 2>&1; then
    echo "ERROR: opkg was not found." >&2
    exit 1
fi

echo "This is a scaffold installer."
echo "The first release package is not published yet."
echo "Next implementation step: download the latest GitHub Release .ipk and install it with opkg."

exit 0
