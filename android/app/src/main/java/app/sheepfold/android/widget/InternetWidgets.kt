package app.sheepfold.android.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import app.sheepfold.android.R
import app.sheepfold.android.router.InternetAccessState
import app.sheepfold.android.router.InternetControlRepository

private const val actionSetInternetEnabled = "app.sheepfold.android.widget.SET_INTERNET_ENABLED"
private const val actionSetInternetDisabled = "app.sheepfold.android.widget.SET_INTERNET_DISABLED"

class InternetEnabledWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        SheepfoldWidgetRenderer.updateEnabledWidgets(context, manager, appWidgetIds)
    }
}

class InternetDisabledWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        SheepfoldWidgetRenderer.updateDisabledWidgets(context, manager, appWidgetIds)
    }
}

class InternetSwitchWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        SheepfoldWidgetRenderer.updateSwitchWidgets(context, manager, appWidgetIds)
    }
}

class SheepfoldWidgetActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            actionSetInternetEnabled -> InternetControlRepository.setInternetState(
                context,
                InternetAccessState.Enabled
            )

            actionSetInternetDisabled -> InternetControlRepository.setInternetState(
                context,
                InternetAccessState.Disabled
            )

            else -> return
        }

        SheepfoldWidgetRenderer.updateAllWidgets(context)
    }
}

object SheepfoldWidgetRenderer {
    fun updateAllWidgets(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        updateEnabledWidgets(
            context,
            manager,
            manager.getAppWidgetIds(ComponentName(context, InternetEnabledWidgetProvider::class.java))
        )
        updateDisabledWidgets(
            context,
            manager,
            manager.getAppWidgetIds(ComponentName(context, InternetDisabledWidgetProvider::class.java))
        )
        updateSwitchWidgets(
            context,
            manager,
            manager.getAppWidgetIds(ComponentName(context, InternetSwitchWidgetProvider::class.java))
        )
    }

    fun updateEnabledWidgets(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val state = InternetControlRepository.readInternetState(context)
        appWidgetIds.forEach { appWidgetId ->
            manager.updateAppWidget(
                appWidgetId,
                singleButtonView(
                    context = context,
                    action = actionSetInternetEnabled,
                    title = "ON",
                    background = if (state == InternetAccessState.Enabled) {
                        R.drawable.widget_button_green_active
                    } else {
                        R.drawable.widget_button_green_inactive
                    }
                )
            )
        }
    }

    fun updateDisabledWidgets(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val state = InternetControlRepository.readInternetState(context)
        appWidgetIds.forEach { appWidgetId ->
            manager.updateAppWidget(
                appWidgetId,
                singleButtonView(
                    context = context,
                    action = actionSetInternetDisabled,
                    title = "OFF",
                    background = if (state == InternetAccessState.Disabled) {
                        R.drawable.widget_button_red_active
                    } else {
                        R.drawable.widget_button_red_inactive
                    }
                )
            )
        }
    }

    fun updateSwitchWidgets(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val state = InternetControlRepository.readInternetState(context)
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(context.packageName, R.layout.widget_internet_switch)
            views.setInt(
                R.id.widgetEnableButton,
                "setBackgroundResource",
                if (state == InternetAccessState.Enabled) {
                    R.drawable.widget_button_green_active
                } else {
                    R.drawable.widget_button_green_inactive
                }
            )
            views.setInt(
                R.id.widgetDisableButton,
                "setBackgroundResource",
                if (state == InternetAccessState.Disabled) {
                    R.drawable.widget_button_red_active
                } else {
                    R.drawable.widget_button_red_inactive
                }
            )
            views.setOnClickPendingIntent(
                R.id.widgetEnableButton,
                widgetAction(context, actionSetInternetEnabled, appWidgetId + 1000)
            )
            views.setOnClickPendingIntent(
                R.id.widgetDisableButton,
                widgetAction(context, actionSetInternetDisabled, appWidgetId + 2000)
            )
            manager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun singleButtonView(
        context: Context,
        action: String,
        title: String,
        background: Int
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_internet_single)
        views.setTextViewText(R.id.widgetButton, title)
        views.setInt(R.id.widgetButton, "setBackgroundResource", background)
        views.setOnClickPendingIntent(R.id.widgetButton, widgetAction(context, action, action.hashCode()))
        return views
    }

    private fun widgetAction(context: Context, action: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, SheepfoldWidgetActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
