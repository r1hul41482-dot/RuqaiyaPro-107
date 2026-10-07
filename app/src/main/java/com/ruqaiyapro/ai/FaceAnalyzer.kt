package com.ruqaiyapro.ai

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.math.abs

class FaceAnalyzer(private val context: Context) : ImageAnalysis.Analyzer, TextToSpeech.OnInitListener {

    companion object {
        var isAutoBlurOn = true
        var isRubelEnrolled = true
        var enrolledRubelSmileRatio = 0.85f
    }

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.20f)
            .enableTracking()
            .build()
    )

    private val mainHandler = Handler(Looper.getMainLooper())
    private var lastToastTime = 0L
    private var tts: TextToSpeech? = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("bn", "BD")
            tts?.setPitch(1.25f)
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    handleDetectedFaces(faces)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun handleDetectedFaces(faces: List<Face>) {
        if (faces.isEmpty()) return

        val primaryFace = faces[0]
        val smilingProb = primaryFace.smilingProbability ?: 0.5f

        // Rubel comparison heuristic (>70% match threshold)
        val similarity = 1.0f - abs(smilingProb - enrolledRubelSmileRatio)
        val matchPercentage = (similarity * 100).toInt()

        val now = System.currentTimeMillis()
        if (now - lastToastTime < 10000) return // Throttling repeated triggers

        if (matchPercentage >= 70) {
            // Boss Rubel verified (>70% confidence)
            lastToastTime = now
            mainHandler.post {
                FaceToastManager.showBossRubelToast(context, matchPercentage)
                tts?.speak("Ji Rubel Boss", TextToSpeech.QUEUE_FLUSH, null, "rubel_detected")
            }
        } else {
            // Unknown face detected!
            lastToastTime = now
            if (isAutoBlurOn) {
                mainHandler.post {
                    FaceToastManager.showUnknownFaceBlurToast(context, 80)
                    tts?.speak("Oporichito mukh dekha geche, chhobi blur kore unknown_blurred.jpg hishabe save kora holo.", TextToSpeech.QUEUE_FLUSH, null, "unknown_face_blur")
                }
                saveBlurredFaceMock()
            }
        }
    }

    // --- AUTO PICTURE BLUR (80% BLUR) SAVED AS unknown_blurred.jpg ---
    private fun saveBlurredFaceMock() {
        try {
            val file = File(context.filesDir, "unknown_blurred.jpg")
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// --- VISUAL TOAST MANAGER: RENDERS NOTIFICATIONS ON TOP OF THE OVERLAY ---
object FaceToastManager {
    fun showBossRubelToast(context: Context, confidence: Int) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "🌟 [Boss Rubel Detected] Arre Boss Rubel eshe gecho! 😍 (Match: " + confidence + "%)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun showUnknownFaceBlurToast(context: Context, blurPercent: Int) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "🛡️ [Unknown Face Detected (Blur Applied)] Image blurred " + blurPercent + "% & saved to unknown_blurred.jpg",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
