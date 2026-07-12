package com.jsoft.f_test.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrayerAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationHelper: NotificationHelper by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: return
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        if (notificationId == -1) return

        notificationHelper.showPrayerNotification(
            id = notificationId,
            prayerName = prayerName,
        )
    }

    companion object {
        const val ACTION_PRAYER_ALARM = "com.jsoft.f_test.action.PRAYER_ALARM"
        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }
}