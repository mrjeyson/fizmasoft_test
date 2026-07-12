package com.jsoft.f_test.data.prayer.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerApi {

    @GET("timings/{date}")
    suspend fun getTimings(
        @Path("date") date: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int = 2,
        @Query("school") school: Int = 1,
    ): PrayerTimesResponse
}