'use strict';
'require view';
'require ui';

return view.extend({
        render: function () {
                return E('div', { 'class': 'cbi-map' }, [
                        E('h2', {}, _('Sheepfold Family Internet Control')),
                        E('p', {}, _('This LuCI view is a placeholder for the first implementation milestone.')),
                        E('div', { 'class': 'right' }, [
                                E('button', {
                                        'class': 'btn cbi-button cbi-button-action',
                                        'click': ui.createHandlerFn(this, function () {
                                                ui.addNotification(null, E('p', {}, _('Block action is not implemented yet.')), 'info');
                                        })
                                }, _('Block internet')),
                                ' ',
                                E('button', {
                                        'class': 'btn cbi-button cbi-button-positive',
                                        'click': ui.createHandlerFn(this, function () {
                                                ui.addNotification(null, E('p', {}, _('Unblock action is not implemented yet.')), 'info');
                                        })
                                }, _('Unblock internet'))
                        ])
                ]);
        }
});
