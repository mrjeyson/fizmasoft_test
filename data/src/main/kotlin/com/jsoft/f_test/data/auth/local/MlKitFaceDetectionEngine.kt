package com.jsoft.f_test.data.auth.local

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.jsoft.f_test.domain.auth.model.FaceDetectionResult
import com.jsoft.f_test.domain.auth.repository.FaceDetectionEngine
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MlKitFaceDetectionEngine : FaceDetectionEngine, ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setMinFaceSize(0.15f)
            .build()
    )

    private val _results = MutableSharedFlow<FaceDetectionResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val results: Flow<FaceDetectionResult> = _results.asSharedFlow()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees,
        )

        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                val result = FaceDetectionResult(
                    faceCount = faces.size,
                    isValid = faces.size == 1,
                )
                _results.tryEmit(result)
            }
            .addOnFailureListener {
                _results.tryEmit(FaceDetectionResult.NoFace)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    override fun release() {
        detector.close()
    }
}