package com.ruqaiyapro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.*
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ruqaiyapro.R
import com.ruqaiyapro.ai.CommandDispatcher
import com.ruqaiyapro.ai.FaceRecognitionManager
import java.util.Locale

class RuqaiyaHotwordService : Service(), TextToSpeech.OnInitListener {

    companion object {
        private const val CHANNEL_ID = "ruqaiya_hotword_channel"
        private const val NOTIF_ID = 1007
        const val WAKE_WORD_1 = "hey ruqaiya"
        const val WAKE_WORD_2 = "hey rukaiya"
        const val WAKE_WORD_3 = "he ruqaiya"
    }

    private lateinit var windowManager: WindowManager
    private var chibiOverlayView: View? = null
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private var faceRecognitionManager: FaceRecognitionManager? = null

    private var isSleeping = true
    private var isListening = false
    private val autoSleepRunnable = Runnable { autoSleepBack() }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        textToSpeech = TextToSpeech(this, this)

        createNotificationChannel()
        startForeground(NOTIF_ID, buildForegroundNotification())

        initChibiFloatingOverlay()
        initGoogleLikeRecognizer()

        // Background FaceRecognitionManager for camera frame processing
        faceRecognitionManager = FaceRecognitionManager(this, textToSpeech)
        faceRecognitionManager?.startBackgroundFaceRecognition()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // ForegroundService with START_STICKY as requested
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ruqaiya Hotword Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Ruqaiya ghumacche... Bolo Hey Ruqaiya"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RuqaiyaPro Active")
            .setContentText("Ruqaiya ghumacche... Bolo Hey Ruqaiya")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    // --- MANDATORY BEHAVIOR 2: NO BEEP TRICK ---
    private fun applyNoBeepTrickAndListen() {
        try {
            // Mute all streams: STREAM_MUSIC, STREAM_SYSTEM, STREAM_NOTIFICATION, STREAM_ALARM, STREAM_RING, STREAM_DTMF
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true)
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, true)
            audioManager.setStreamMute(AudioManager.STREAM_RING, true)
            audioManager.setStreamMute(AudioManager.STREAM_DTMF, true)

            // Handler.postDelayed 500ms startListening
            mainHandler.postDelayed({
                startListeningInternal()
            }, 500)
        } catch (e: Exception) {
            e.printStackTrace()
            unMuteAllStreams()
        }
    }

    private fun unMuteAllStreams() {
        try {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, false)
            audioManager.setStreamMute(AudioManager.STREAM_RING, false)
            audioManager.setStreamMute(AudioManager.STREAM_DTMF, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initGoogleLikeRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) return

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}

                override fun onError(error: Int) {
                    unMuteAllStreams()
                    isListening = false
                    // In onError: unMute all then Handler.postDelayed 1500ms restart. No loop crash.
                    mainHandler.postDelayed({
                        if (isSleeping) {
                            applyNoBeepTrickAndListen()
                        }
                    }, 1500)
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val spokenText = matches?.firstOrNull()?.lowercase(Locale.ROOT) ?: return

                    // Live Toast like Google: onPartialResults Toast "Tumi bolcho: {text}"
                    if (!isSleeping) {
                        Toast.makeText(applicationContext, "Tumi bolcho: $spokenText", Toast.LENGTH_SHORT).show()
                    }

                    // Check wake word if sleeping
                    if (isSleeping) {
                        if (spokenText.contains(WAKE_WORD_1) || spokenText.contains(WAKE_WORD_2) || spokenText.contains(WAKE_WORD_3)) {
                            onWakeWordDetected()
                        }
                    }
                }

                override fun onResults(results: Bundle?) {
                    unMuteAllStreams()
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val finalSpeech = matches?.firstOrNull() ?: ""

                    // Live Toast like Google: onResults Toast "Tumi bolle: {final}"
                    if (finalSpeech.isNotEmpty()) {
                        Toast.makeText(applicationContext, "Tumi bolle: $finalSpeech", Toast.LENGTH_SHORT).show()
                    }

                    if (isSleeping) {
                        val lower = finalSpeech.lowercase(Locale.ROOT)
                        if (lower.contains(WAKE_WORD_1) || lower.contains(WAKE_WORD_2) || lower.contains(WAKE_WORD_3)) {
                            onWakeWordDetected()
                        } else {
                            // Restart wake listening
                            mainHandler.postDelayed({
                                applyNoBeepTrickAndListen()
                            }, 1500)
                        }
                    } else {
                        // In Command mode: process Boss Rubel's instruction
                        handleBossCommand(finalSpeech)
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        applyNoBeepTrickAndListen()
    }

    private fun startListeningInternal() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        speechRecognizer?.startListening(intent)
    }

    // --- MANDATORY BEHAVIOR 3: WAKE EXACTLY LIKE GOOGLE ---
    private fun onWakeWordDetected() {
        isSleeping = false

        // Vibrate 50ms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }

        // Chibi 80dp -> 320dp bounce alpha 1.0 glow
        expandChibiToActive()

        // UnMute all streams
        unMuteAllStreams()

        // Toast "🎤 Ji Rubel Boss bolo..." + TTS "Ji Rubel Boss bolo"
        Toast.makeText(applicationContext, "🎤 Ji Rubel Boss bolo...", Toast.LENGTH_SHORT).show()
        speakGreeting("Ji Rubel Boss bolo")

        // Start commandRecognizer (MIC ON) after brief TTS delay
        mainHandler.postDelayed({
            startListeningInternal()
            scheduleAutoSleep()
        }, 1200)
    }

    private fun handleBossCommand(command: String) {
        val reply = CommandDispatcher.execute(applicationContext, command)
        
        // Live Toast like Google: reply Toast "Ruqaiya: {reply}" + TTS
        Toast.makeText(applicationContext, "Ruqaiya: $reply", Toast.LENGTH_LONG).show()
        speakGreeting(reply)

        // Reset auto sleep timer to 4 sec
        scheduleAutoSleep()
    }

    private fun scheduleAutoSleep() {
        mainHandler.removeCallbacks(autoSleepRunnable)
        mainHandler.postDelayed(autoSleepRunnable, 4000) // 4 sec auto sleep
    }

    private fun autoSleepBack() {
        isSleeping = true
        collapseChibiToSleeping()
        applyNoBeepTrickAndListen()
    }

    // --- FLOATING CHIBI WINDOW MANAGER OVERLAY (80dp sleep alpha 0.20 -> 320dp active) ---
    private fun initChibiFloatingOverlay() {
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            dpToPx(80),
            dpToPx(80),
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            x = dpToPx(16)
            y = dpToPx(60)
            alpha = 0.20f // 80dp alpha 0.20 floating when sleeping
        }

        chibiOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_chibi, null).apply {
            setOnClickListener {
                if (isSleeping) {
                    onWakeWordDetected()
                } else {
                    autoSleepBack()
                }
            }
        }

        try {
            windowManager.addView(chibiOverlayView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun expandChibiToActive() {
        chibiOverlayView?.let { view ->
            val params = view.layoutParams as WindowManager.LayoutParams
            params.width = dpToPx(320)
            params.height = dpToPx(320)
            params.alpha = 1.0f // Alpha 1.0 glow
            params.gravity = Gravity.CENTER
            windowManager.updateViewLayout(view, params)
        }
    }

    private fun collapseChibiToSleeping() {
        chibiOverlayView?.let { view ->
            val params = view.layoutParams as WindowManager.LayoutParams
            params.width = dpToPx(80)
            params.height = dpToPx(80)
            params.alpha = 0.20f // Back to 80dp alpha 0.20
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.x = dpToPx(16)
            params.y = dpToPx(60)
            windowManager.updateViewLayout(view, params)
        }
    }

    private fun speakGreeting(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ruqaiya_tts")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale("bn", "BD")
            textToSpeech?.setPitch(1.25f) // Sweet anime tone for Ruqaiya
            textToSpeech?.setSpeechRate(0.95f)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        unMuteAllStreams()
        faceRecognitionManager?.stop()
        speechRecognizer?.destroy()
        textToSpeech?.shutdown()
        chibiOverlayView?.let { windowManager.removeView(it) }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
}
