package com.jsoft.f_test.domain.auth.model

data class FaceDetectionResult(
    val faceCount: Int,
    val isValid: Boolean,
) {
    companion object {
        val NoFace = FaceDetectionResult(faceCount = 0, isValid = false)
    }
}