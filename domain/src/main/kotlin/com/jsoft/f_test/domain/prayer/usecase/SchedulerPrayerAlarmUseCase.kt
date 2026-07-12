package com.jsoft.f_test.domain.prayer.usecase

import com.jsoft.f_test.domain.prayer.model.PrayerName
import com.jsoft.f_test.domain.prayer.model.PrayerTimes
import com.jsoft.f_test.domain.prayer.scheduler.AlarmScheduler
import com.jsoft.f_test.domain.prayer.scheduler.PrayerAlarmIds

class SchedulePrayerAlarmsUseCase(
    private val alarmScheduler: AlarmScheduler,
) {
    operator fun invoke(prayerTimes: PrayerTimes) {
        prayerTimes.asList().forEach { prayer ->
            val id = prayer.name.toAlarmId()
            alarmScheduler.schedulePrayerAlarm(
                id = id,
                timeMillis = prayer.timeMillis,
                prayerName = prayer.name.toDisplayName(),
            )
        }
    }

    private fun PrayerName.toAlarmId(): Int = when (this) {
        PrayerName.Fajr -> PrayerAlarmIds.FAJR
        PrayerName.Dhuhr -> PrayerAlarmIds.DHUHR
        PrayerName.Asr -> PrayerAlarmIds.ASR
        PrayerName.Maghrib -> PrayerAlarmIds.MAGHRIB
        PrayerName.Isha -> PrayerAlarmIds.ISHA
    }

    private fun PrayerName.toDisplayName(): String = when (this) {
        PrayerName.Fajr -> "Bomdod"
        PrayerName.Dhuhr -> "Peshin"
        PrayerName.Asr -> "Asr"
        PrayerName.Maghrib -> "Shom"
        PrayerName.Isha -> "Xufton"
    }
}