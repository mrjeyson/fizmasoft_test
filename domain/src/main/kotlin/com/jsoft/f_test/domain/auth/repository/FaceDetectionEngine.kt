package com.jsoft.f_test.domain.auth.repository

import com.jsoft.f_test.domain.auth.model.FaceDetectionResult
import kotlinx.coroutines.flow.Flow

interface FaceDetectionEngine {
    val results: Flow<FaceDetectionResult>

    fun release()
}