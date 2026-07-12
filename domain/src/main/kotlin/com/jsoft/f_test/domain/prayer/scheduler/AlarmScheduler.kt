package com.jsoft.f_test.domain.prayer.scheduler

interface AlarmScheduler {

    fun schedulePrayerAlarm(
        id: Int,
        timeMillis: Long,
        prayerName: String,
    ): Boolean

    fun cancelAlarm(id: Int)

    fun cancelAllPrayerAlarms()

    fun canScheduleExactAlarms(): Boolean
}

object PrayerAlarmIds {
    const val FAJR = 1000
    const val DHUHR = 1001
    const val ASR = 1002
    const val MAGHRIB = 1003
    const val ISHA = 1004

    val ALL = listOf(FAJR, DHUHR, ASR, MAGHRIB, ISHA)
}