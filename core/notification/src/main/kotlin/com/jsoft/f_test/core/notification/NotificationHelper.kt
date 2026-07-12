package com.jsoft.f_test.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(
    private val context: Context,
) {
    init {
        createPrayerChannel()
    }

    private fun createPrayerChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID_PRAYER,
            "Namoz vaqtlari",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Namoz vaqtlari kelganda eslatma"
            enableVibration(true)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    fun showPrayerNotification(id: Int, prayerName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_PRAYER)
            .setSmallIcon(android.R.drawable.star_on)
            .setContentTitle("$prayerName vaqti")
            .setContentText("$prayerName namozi vaqti kirdi")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
        }
    }

    companion object {
        const val CHANNEL_ID_PRAYER = "prayer_times"
    }
}