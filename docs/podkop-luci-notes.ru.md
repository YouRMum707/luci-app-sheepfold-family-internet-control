# Заметки по реализации LuCI в Podkop

Дата просмотра: 2026-07-02.

Источники:

- https://github.com/itdoginfo/podkop/tree/main/luci-app-podkop
- https://github.com/itdoginfo/podkop/tree/main/fe-app-podkop/src

## Что важно перенять

Podkop разделяет LuCI-приложение на две части:

- `luci-app-podkop` — OpenWRT/LuCI package: Makefile, menu, ACL, compiled JS, uci-defaults;
- `fe-app-podkop` — исходники frontend на TypeScript, которые собираются в LuCI JS modules.

Для Sheepfold стоит сделать похожую структуру, когда визуальный прототип будет заменяться рабочим интерфейсом:

```text
fe-app-sheepfold/src/
  sheepfold/
    methods/
    services/
    tabs/
    validators/
    helpers/
package/luci-app-sheepfold-family-internet-control/
  htdocs/luci-static/resources/view/sheepfold/
  root/usr/share/luci/menu.d/
  root/usr/share/rpcd/acl.d/
```

## Entry point

Podkop entry point создаёт стандартный LuCI `form.Map`, включает `tabbed = true`, а содержимое вкладок делегирует отдельным модулям:

```text
podkop.js
  -> main.injectGlobalStyles()
  -> new form.Map("podkop", ...)
  -> section.createSectionContent(...)
  -> settings.createSettingsContent(...)
  -> diagnostic.createDiagnosticContent(...)
  -> dashboard.createDashboardContent(...)
  -> main.coreService()
```

Для Sheepfold лучше сделать так же:

```text
overview.js
  -> new form.Map("sheepfold", ...)
  -> devices.createDevicesContent(...)
  -> allowlist.createAllowlistContent(...)
  -> blocklist.createBlocklistContent(...)
  -> schedules.createSchedulesContent(...)
  -> emergencySites.createEmergencySitesContent(...)
  -> wifi.createWifiContent(...)
  -> integrations.createIntegrationsContent(...)
  -> messaging.createMessagingContent(...)
  -> logs.createLogsContent(...)
  -> settings.createSettingsContent(...)
```

Это лучше текущего большого `overview.js`, потому что вкладки Sheepfold будут сложными.

## Backend вызовы

Podkop не даёт LuCI выполнять произвольные команды. UI вызывает типизированный набор методов, а методы вызывают ограниченный shell wrapper:

```text
fs.exec("/usr/bin/podkop", ["get_status"])
fs.exec("/usr/bin/podkop", ["check_nft_rules"])
fs.exec("/etc/init.d/podkop", ["restart"])
```

Ответы возвращаются как JSON или stdout.

Для Sheepfold нужен такой же слой:

```text
/usr/bin/sheepfold status
/usr/bin/sheepfold list_devices
/usr/bin/sheepfold get_device <mac>
/usr/bin/sheepfold apply_rules
/usr/bin/sheepfold diagnostics
/usr/bin/sheepfold export --masked
/usr/bin/sheepfold import <file>
/usr/bin/sheepfold clear_logs
/usr/bin/sheepfold messenger_status
```

LuCI должен вызывать только этот белый список методов, а не собирать shell-команды из пользовательского ввода.

## ACL

Podkop ACL явно разрешает:

- чтение UCI `podkop`;
- запись UCI `podkop`;
- `exec` только для `/usr/bin/podkop` и `/etc/init.d/podkop`;
- чтение `ubus service list`.

Для Sheepfold нужен такой же минимальный ACL:

- UCI: `sheepfold`, `dhcp`, `wireless`;
- file exec: `/usr/bin/sheepfold`, `/etc/init.d/sheepfold`;
- ubus service list для статусов сервисов;
- дополнительные exec/file permissions только если без них нельзя реализовать конкретную вкладку.

## Диагностика

Podkop держит диагностику отдельной вкладкой и запускает checks через backend methods:

- DNS check;
- FakeIP check;
- nft rules check;
- sing-box check;
- system info.

Для Sheepfold нужна похожая вкладка:

- nftables table/rules exist;
- device sets актуальны;
- DHCP/static leases читаются;
- blocked page service работает;
- AdGuard Home обнаружен и режим совместимости понятен;
- Podkop обнаружен и Sheepfold стоит перед ним в цепочке;
- messenger adapter status;
- log storage status.

## Store и live UI

Podkop использует маленький frontend store, подписки и lifecycle по активной вкладке. Это нужно для dashboard/logs, чтобы не обновлять всё приложение при каждом событии.

Для Sheepfold стоит применить это к:

- вкладке `Все устройства`;
- журналу;
- диагностике;
- статусам интеграций;
- бот-статусу.

## Сборка frontend

Podkop пишет frontend на TypeScript и собирает его через `tsup` в LuCI-compatible JS. После сборки файл патчится в `baseclass.extend(...)`, чтобы его можно было подключать через `require view.podkop.main`.

Для Sheepfold можно начать проще на plain LuCI JS, но при росте интерфейса лучше перейти на такую схему:

- исходники в `fe-app-sheepfold/src`;
- сборка в `package/.../htdocs/luci-static/resources/view/sheepfold`;
- validators/helpers/services отдельно;
- локализация через LuCI `_()`.

## Cache

Podkop очищает LuCI cache через uci-defaults:

```text
rm -f /var/luci-indexcache*
rm -f /tmp/luci-indexcache*
rpcd reload
```

Для Sheepfold нужно оставить наш `ui_asset_version`, но также добавить uci-defaults/postinst очистку LuCI index cache после установки и обновления.

## Вывод

Текущий visual prototype Sheepfold можно оставить для просмотра, но рабочую LuCI-реализацию надо перестраивать по Podkop-подобной архитектуре:

- стандартный `form.Map` + `tabbed = true`;
- каждая вкладка в отдельном модуле;
- backend command wrapper `/usr/bin/sheepfold`;
- строгий rpcd ACL;
- диагностика отдельной вкладкой;
- frontend store только там, где нужен live UI;
- cache cleanup + asset versioning.
