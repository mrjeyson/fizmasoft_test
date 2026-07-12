package com.jsoft.f_test.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jsoft.f_test.domain.prayer.scheduler.AlarmScheduler
import com.jsoft.f_test.domain.prayer.scheduler.PrayerAlarmIds

class AlarmSchedulerImpl(
    private val context: Context,
) : AlarmScheduler {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedulePrayerAlarm(
        id: Int,
        timeMillis: Long,
        prayerName: String,
    ): Boolean {
        if (timeMillis <= System.currentTimeMillis()) {
            return true
        }
        if (!canScheduleExactAlarms()) {
            return false
        }

        val pendingIntent = createPendingIntent(id, prayerName)

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMillis,
                pendingIntent,
            )
            return true
        } catch (e: SecurityException) {
            return false
        }
    }

    override fun cancelAlarm(id: Int) {
        val pendingIntent = createPendingIntent(id, prayerName = "")
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    override fun cancelAllPrayerAlarms() {
        PrayerAlarmIds.ALL.forEach { cancelAlarm(it) }
    }

    override fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun createPendingIntent(id: Int, prayerName: String): PendingIntent {
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_NAME, prayerName)
            putExtra(PrayerAlarmReceiver.EXTRA_NOTIFICATION_ID, id)
        }

        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}