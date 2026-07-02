'use strict';
'require view';
'require ui';

var devices = [
        {
                name: 'Parent phone',
                ip: '192.168.1.21',
                mac: 'A4:5E:60:12:34:56',
                group: 'Parents',
                status: 'allow',
                note: 'Always available, admin device'
        },
        {
                name: 'Child tablet',
                ip: '192.168.1.43',
                mac: '58:2F:40:AA:18:10',
                group: 'Children',
                status: 'scheduled',
                note: 'School-day schedule, bedtime 21:00'
        },
        {
                name: 'Living room TV',
                ip: '192.168.1.77',
                mac: 'F0:99:BF:70:22:09',
                group: 'TVs / media',
                status: 'restricted',
                note: 'Allowed after homework window'
        },
        {
                name: 'Unknown device',
                ip: '192.168.1.98',
                mac: 'DC:A6:32:8C:00:19',
                group: 'Unconfigured',
                status: 'new',
                note: 'Detected from router leases'
        },
        {
                name: 'Old game console',
                ip: '192.168.1.64',
                mac: '00:1F:16:CC:90:02',
                group: 'Children',
                status: 'blocked',
                note: 'Blocklist'
        }
];

var emergencySites = [
        ['gosuslugi.ru', 'Gosuslugi', 'Government services'],
        ['esia.gosuslugi.ru', 'ESIA', 'Government account login'],
        ['mos.ru', 'Moscow services', 'City services'],
        ['school.mos.ru', 'Moscow school', 'School access'],
        ['dnevnik.ru', 'Dnevnik.ru', 'School diary'],
        ['ya.ru', 'Yandex search', 'Narrow search entrypoint'],
        ['2gis.ru', '2GIS', 'Maps and local organizations']
];

var tabs = [
        ['devices', _('All devices')],
        ['allowlist', _('Allowlist')],
        ['blocklist', _('Blocklist')],
        ['schedules', _('Schedules')],
        ['emergency', _('Emergency-useful sites')],
        ['wifi', _('Wi-Fi')],
        ['integrations', _('Integrations')],
        ['bot', _('Bot and admins')],
        ['logs', _('Logs')],
        ['settings', _('Settings')]
];

function notify(message, level) {
        ui.addNotification(null, E('p', {}, message), level || 'info');
}

function badge(status) {
        var labels = {
                allow: _('Allowlist'),
                blocked: _('Blocklist'),
                scheduled: _('Scheduled'),
                restricted: _('Restricted'),
                new: _('New')
        };

        return E('span', { 'class': 'sf-badge sf-badge-' + status }, labels[status] || status);
}

function metric(label, value, tone) {
        return E('div', { 'class': 'sf-metric sf-metric-' + tone }, [
                E('span', {}, label),
                E('strong', {}, value)
        ]);
}

function actionButton(label, tone, message) {
        return E('button', {
                'class': 'sf-action sf-action-' + tone,
                'click': function (ev) {
                        ev.preventDefault();
                        notify(message || _('This action is a visual prototype only.'), tone === 'danger' ? 'warning' : 'info');
                }
        }, label);
}

function deviceTable(rows, options) {
        options = options || {};

        var tableRows = rows.map(function (device) {
                return E('div', { 'class': 'sf-device-row' }, [
                        E('div', { 'class': 'sf-device-name' }, [
                                        E('strong', {}, device.name),
                                        E('small', {}, device.note)
                        ]),
                        E('div', {}, device.ip),
                        E('div', { 'class': 'sf-mono' }, device.mac),
                        E('div', {}, device.group),
                        E('div', {}, badge(device.status)),
                        E('div', { 'class': 'sf-row-actions' }, [
                                actionButton(_('Configure'), 'neutral', _('Device editor is not implemented in this visual test build.')),
                                options.compact ? '' : actionButton(_('+30 min'), 'positive', _('Temporary access would require confirmation.'))
                        ])
                ]);
        });

        return E('div', { 'class': 'sf-device-table' }, [
                E('div', { 'class': 'sf-device-row sf-device-head' }, [
                        E('div', {}, _('Device')),
                        E('div', {}, _('IP address')),
                        E('div', {}, _('MAC address')),
                        E('div', {}, _('Group')),
                        E('div', {}, _('Status')),
                        E('div', {}, _('Actions'))
                ])
        ].concat(tableRows));
}

function field(label, value, hint) {
        return E('label', { 'class': 'sf-field' }, [
                E('span', {}, label),
                E('input', { 'class': 'cbi-input-text', 'value': value || '' }),
                hint ? E('small', {}, hint) : ''
        ]);
}

function selectField(label, value, values, hint) {
        return E('label', { 'class': 'sf-field' }, [
                E('span', {}, label),
                E('select', { 'class': 'cbi-input-select' }, values.map(function (item) {
                        return E('option', { 'value': item[0], 'selected': item[0] === value ? 'selected' : null }, item[1]);
                })),
                hint ? E('small', {}, hint) : ''
        ]);
}

return view.extend({
        activeTab: 'devices',

        switchTab: function (button, tab) {
                var page = button.closest('.sf-page');

                this.activeTab = tab;

                page.querySelectorAll('.sf-tab').forEach(function (node) {
                        node.classList.toggle('active', node.getAttribute('data-tab') === tab);
                });

                page.querySelectorAll('.sf-tab-panel').forEach(function (node) {
                        node.hidden = node.getAttribute('data-tab') !== tab;
                });
        },

        renderTabs: function () {
                var self = this;

                return E('div', { 'class': 'sf-tabs' }, tabs.map(function (tab) {
                        return E('button', {
                                'class': 'sf-tab' + (self.activeTab === tab[0] ? ' active' : ''),
                                'data-tab': tab[0],
                                'click': function (ev) {
                                        ev.preventDefault();
                                        self.switchTab(ev.currentTarget, tab[0]);
                                }
                        }, tab[1]);
                }));
        },

        renderDevices: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('All devices')),
                                        E('p', {}, _('Detected automatically from router leases, ARP/neighbor data, and static DHCP leases.'))
                                ]),
                                E('div', { 'class': 'sf-toolbar' }, [
                                        E('input', {
                                                'class': 'cbi-input-text sf-search',
                                                'placeholder': _('Search by name, IP, or MAC')
                                        }),
                                        actionButton(_('Add manually'), 'positive', _('Manual MAC-based add form is not implemented in this visual test build.'))
                                ])
                        ]),
                        deviceTable(devices)
                ]);
        },

        renderAllowlist: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Allowlist')),
                                        E('p', {}, _('These devices are never blocked by global blocking or schedules.'))
                                ]),
                                actionButton(_('Add device'), 'positive', _('The UI must prevent adding the same MAC to allowlist and blocklist.'))
                        ]),
                        deviceTable(devices.filter(function (device) { return device.status === 'allow'; }), { compact: true })
                ]);
        },

        renderBlocklist: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Blocklist')),
                                        E('p', {}, _('Blocklisted devices cannot access the internet, LuCI, SSH, or the Sheepfold API.'))
                                ]),
                                actionButton(_('Add device'), 'danger', _('Blocklist changes require confirmation.'))
                        ]),
                        E('div', { 'class': 'sf-note sf-note-warning' }, _('Emergency-useful sites for blocklisted devices require a separate explicit setting and still do not open router access.')),
                        deviceTable(devices.filter(function (device) { return device.status === 'blocked'; }), { compact: true })
                ]);
        },

        renderSchedules: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Schedules')),
                                        E('p', {}, _('Allow and block rules for devices and groups.'))
                                ]),
                                actionButton(_('Add rule'), 'positive', _('Schedule editor is not implemented in this visual test build.'))
                        ]),
                        E('div', { 'class': 'sf-grid two' }, [
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('School days')),
                                        E('p', {}, _('Children group')),
                                        E('strong', {}, _('Allow 07:00-20:30, block after bedtime'))
                                ]),
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('Temporary access')),
                                        E('div', { 'class': 'sf-chip-row' }, [
                                                '+15', '+30', '+1h', '+2h', '+3h', '+5h', _('End of day'), _('Bedtime')
                                        ].map(function (label) {
                                                return E('button', {
                                                'class': 'sf-chip',
                                                        'click': function (ev) {
                                                                ev.preventDefault();
                                                                notify(_('Temporary access requires confirmation.'), 'info');
                                                        }
                                                }, label);
                                        }))
                                ])
                        ]),
                        E('div', { 'class': 'sf-form-row' }, [
                                field(_('Default bedtime'), '21:00', _('Used by the "until bedtime" quick action.'))
                        ])
                ]);
        },

        renderEmergency: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Access to emergency-useful sites')),
                                        E('p', {}, _('Editable list for necessary services during restricted access.'))
                                ]),
                                actionButton(_('Add domain'), 'positive', _('Domain editor is not implemented in this visual test build.'))
                        ]),
                        E('div', { 'class': 'sf-note' }, _('Do not add broad yandex.ru by default: it can open video, music, games, feeds, and other non-emergency services.')),
                        E('div', { 'class': 'sf-domain-list' }, emergencySites.map(function (site) {
                                return E('div', { 'class': 'sf-domain' }, [
                                        E('strong', {}, site[0]),
                                        E('span', {}, site[1]),
                                        E('small', {}, site[2])
                                ]);
                        }))
                ]);
        },

        renderWifi: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Wi-Fi')),
                                        E('p', {}, _('Family-facing shortcut for common OpenWRT wireless settings.'))
                                ]),
                                actionButton(_('Apply Wi-Fi changes'), 'danger', _('Wi-Fi changes can disconnect current users and require confirmation.'))
                        ]),
                        E('div', { 'class': 'sf-grid two' }, [
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('2.4 GHz')),
                                        field(_('SSID'), 'Sheepfold Home 2G'),
                                        field(_('Password'), '********'),
                                        selectField(_('Security'), 'sae-mixed', [
                                                ['sae-mixed', 'WPA2/WPA3 mixed'],
                                                ['psk2', 'WPA2-PSK'],
                                                ['sae', 'WPA3-SAE']
                                        ]),
                                        selectField(_('Channel'), 'auto', [
                                                ['auto', _('Auto')],
                                                ['1', '1'],
                                                ['6', '6'],
                                                ['11', '11']
                                        ])
                                ]),
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('5 GHz')),
                                        field(_('SSID'), 'Sheepfold Home 5G'),
                                        field(_('Password'), '********'),
                                        selectField(_('Security'), 'sae-mixed', [
                                                ['sae-mixed', 'WPA2/WPA3 mixed'],
                                                ['psk2', 'WPA2-PSK'],
                                                ['sae', 'WPA3-SAE']
                                        ]),
                                        selectField(_('Channel'), 'auto', [
                                                ['auto', _('Auto')],
                                                ['36', '36'],
                                                ['44', '44'],
                                                ['149', '149']
                                        ])
                                ])
                        ])
                ]);
        },

        renderIntegrations: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('h3', {}, _('Integrations')),
                        E('div', { 'class': 'sf-form-row' }, [
                                selectField(_('Use together with'), 'adguard_podkop', [
                                        ['none', _('None')],
                                        ['adguard', 'AdGuard Home'],
                                        ['podkop', 'Podkop'],
                                        ['adguard_podkop', 'AdGuard Home + Podkop']
                                ], _('Traffic order: Sheepfold -> AdGuard Home -> Podkop.'))
                        ]),
                        E('div', { 'class': 'sf-note' }, _('Automatic router changes must show integration-specific notes and create/export a backup before applying.'))
                ]);
        },

        renderBot: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('h3', {}, _('Bot and administrators')),
                        E('div', { 'class': 'sf-grid two' }, [
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('Messenger')),
                                        selectField(_('Active messenger'), 'none', [
                                                ['none', _('Disabled')],
                                                ['vk', 'VK'],
                                                ['telegram', 'Telegram'],
                                                ['max', 'MAX experimental']
                                        ], _('VK is shown first during setup, but activation requires credentials and an approved admin.')),
                                        field(_('Approved admin ID'), 'vk:123***789', _('Stored on the router.'))
                                ]),
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('Commands')),
                                        E('div', { 'class': 'sf-command-list' }, [
                                                _('show all devices'),
                                                _('block internet'),
                                                _('unblock internet'),
                                                _('grant +30 minutes'),
                                                _('status')
                                        ].map(function (command) {
                                                return E('code', {}, command);
                                        }))
                                ])
                        ])
                ]);
        },

        renderLogs: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('div', { 'class': 'sf-panel-head' }, [
                                E('div', {}, [
                                        E('h3', {}, _('Logs')),
                                        E('p', {}, _('Administrative action log with masking.'))
                                ]),
                                E('div', { 'class': 'sf-toolbar' }, [
                                        actionButton(_('Clear log'), 'danger', _('Clearing logs requires confirmation.')),
                                        actionButton(_('Export masked'), 'neutral', _('Masked log export is not implemented in this visual test build.'))
                                ])
                        ]),
                        E('div', { 'class': 'sf-log' }, [
                                E('div', {}, [E('time', {}, '20:31'), E('span', {}, _('admin granted +30 minutes to Child tablet'))]),
                                E('div', {}, [E('time', {}, '19:55'), E('span', {}, _('new device detected: DC:A6:32:xx:xx:19'))]),
                                E('div', {}, [E('time', {}, '18:10'), E('span', {}, _('global block disabled by owner'))])
                        ])
                ]);
        },

        renderSettings: function () {
                return E('div', { 'class': 'sf-panel' }, [
                        E('h3', {}, _('Settings')),
                        E('div', { 'class': 'sf-grid two' }, [
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('General')),
                                        selectField(_('Language'), 'ru', [
                                                ['ru', _('Russian')],
                                                ['en', _('English')]
                                        ]),
                                        selectField(_('New device behavior'), 'allow', [
                                                ['allow', _('Allow internet by default')],
                                                ['restrict_until_configured', _('Restrict until configured')]
                                        ]),
                                        selectField(_('Known offline devices cleanup'), '90', [
                                                ['30', _('30 days')],
                                                ['90', _('90 days')],
                                                ['180', _('180 days')]
                                        ])
                                ]),
                                E('div', { 'class': 'sf-box' }, [
                                        E('h4', {}, _('Export and update')),
                                        selectField(_('Export mode'), 'safe', [
                                                ['safe', _('Readable JSON without secrets')],
                                                ['encrypted', _('Encrypted full backup')]
                                        ]),
                                        field(_('Blocked page text'), _('Internet is temporarily unavailable by family rules.')),
                                        E('div', { 'class': 'sf-toolbar' }, [
                                                actionButton(_('Update app'), 'danger', _('Application update requires confirmation.')),
                                                actionButton(_('Reboot router'), 'danger', _('Router reboot requires confirmation.'))
                                        ])
                                ])
                        ])
                ]);
        },

        renderPanel: function (tab, content) {
                return E('section', {
                        'class': 'sf-tab-panel',
                        'data-tab': tab,
                        'hidden': this.activeTab === tab ? null : 'hidden'
                }, content);
        },

        renderPanels: function () {
                return [
                        this.renderPanel('devices', this.renderDevices()),
                        this.renderPanel('allowlist', this.renderAllowlist()),
                        this.renderPanel('blocklist', this.renderBlocklist()),
                        this.renderPanel('schedules', this.renderSchedules()),
                        this.renderPanel('emergency', this.renderEmergency()),
                        this.renderPanel('wifi', this.renderWifi()),
                        this.renderPanel('integrations', this.renderIntegrations()),
                        this.renderPanel('bot', this.renderBot()),
                        this.renderPanel('logs', this.renderLogs()),
                        this.renderPanel('settings', this.renderSettings())
                ];
        },

        render: function () {
                var assetVersion = '0.1.0-4';
                var cssHref = L.resource('sheepfold/sheepfold.css') + '?v=' + encodeURIComponent(assetVersion);

                return E('div', { 'class': 'sf-page' }, [
                        E('link', { 'rel': 'stylesheet', 'href': cssHref }),
                        E('div', { 'class': 'sf-header' }, [
                                E('div', {}, [
                                        E('h2', {}, _('Sheepfold Family Internet Control')),
                                        E('p', {}, _('Visual test build. Router rules and persistence are not active yet.'))
                                ]),
                                E('div', { 'class': 'sf-header-actions' }, [
                                        actionButton(_('Block internet'), 'danger', _('Global block would block every device except allowlist.')),
                                        actionButton(_('Unblock'), 'positive', _('Global block would be disabled after confirmation.')),
                                        actionButton(_('Export'), 'neutral', _('Default export is readable JSON without secrets.')),
                                        actionButton(_('Import'), 'neutral', _('Import requires confirmation.'))
                                ])
                        ]),
                        E('div', { 'class': 'sf-metrics' }, [
                                metric(_('Devices'), '5', 'neutral'),
                                metric(_('Allowlist'), '1', 'positive'),
                                metric(_('Restricted'), '2', 'warning'),
                                metric(_('Blocklist'), '1', 'danger')
                        ]),
                        this.renderTabs(),
                        E('div', { 'class': 'sf-panels' }, this.renderPanels())
                ]);
        }
});
