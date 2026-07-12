package com.jsoft.f_test.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jsoft.f_test.domain.prayer.repository.PrayerRepository
import com.jsoft.f_test.domain.prayer.usecase.SchedulePrayerAlarmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BootReceiver : BroadcastReceiver(), KoinComponent {
    private val prayerRepository: PrayerRepository by inject()
    private val schedulePrayerAlarms: SchedulePrayerAlarmsUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("BootReceiver", "onReceive: ${intent.action}")

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val prayerTimes = prayerRepository.getPrayerTimes(today)
                android.util.Log.d("BootReceiver", "PrayerTimes: $prayerTimes")
                if (prayerTimes != null) {
                    schedulePrayerAlarms(prayerTimes)
                    android.util.Log.d("BootReceiver", "Alarms rescheduled")
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}