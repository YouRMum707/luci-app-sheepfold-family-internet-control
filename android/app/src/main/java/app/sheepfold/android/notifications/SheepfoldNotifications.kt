package app.sheepfold.android.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import app.sheepfold.android.MainActivity
import app.sheepfold.android.R

data class NewDeviceNotification(
    val id: Int,
    val name: String,
    val ip: String,
    val mac: String
)

object SheepfoldNotifications {
    private const val channelId = "sheepfold_devices"
    private const val channelName = "Sheepfold devices"
    private const val notifiedDevicesPrefs = "sheepfold_notified_devices"

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Уведомления о новых устройствах в домашней сети"
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun notifyNewDeviceOnce(context: Context, device: NewDeviceNotification) {
        val appContext = context.applicationContext
        val notifiedPrefs = appContext.getSharedPreferences(notifiedDevicesPrefs, Context.MODE_PRIVATE)
        val notifiedKey = "device_${device.id}_${device.mac}"
        if (notifiedPrefs.getBoolean(notifiedKey, false)) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                return
            }
        }

        val openAppIntent = Intent(appContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            device.id,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Обнаружено новое устройство")
            .setContentText("#${device.id} ${device.name}, IP ${device.ip}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("#${device.id} ${device.name}, IP ${device.ip}, MAC ${device.mac}")
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(appContext)
            .notify(10_000 + device.id, notification)

        notifiedPrefs.edit()
            .putBoolean(notifiedKey, true)
            .apply()
    }
}
