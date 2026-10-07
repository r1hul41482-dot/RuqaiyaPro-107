package com.ruqaiyapro.ai

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ruqaiyapro.system.PhoneControlManager
import java.util.Locale

object CommandDispatcher {

    private var pendingCriticalAction: String? = null
    private var pendingActionType: String? = null
    private var pendingActionTimestamp: Long = 0
    private val mainHandler = Handler(Looper.getMainLooper())

    private val timeoutRunnable = Runnable {
        if (pendingCriticalAction != null) {
            pendingCriticalAction = null
            pendingActionType = null
            // Informs the user when 5s timeout expires
            Toast.makeText(appContext, "5 second par hoyeche. Kaj batil kora holo Boss.", Toast.LENGTH_SHORT).show()
        }
    }

    private var appContext: Context? = null

    fun execute(context: Context, command: String): String {
        appContext = context.applicationContext
        val lower = command.lowercase(Locale.ROOT)
        val phoneControl = PhoneControlManager(context)

        // --- STEP 1: CHECK IF CONFIRMING PENDING CRITICAL ACTION ---
        if (pendingCriticalAction != null) {
            val elapsed = System.currentTimeMillis() - pendingActionTimestamp
            if (elapsed <= 5000) {
                mainHandler.removeCallbacks(timeoutRunnable)
                if (lower.contains("haa") || lower.contains("koro") || lower.contains("yes") || lower.contains("shuru koro")) {
                    val action = pendingActionType
                    pendingCriticalAction = null
                    pendingActionType = null
                    return when (action) {
                        "whatsapp_broadcast" -> {
                            "Boss er nirdeshe WhatsApp broadcast shuru kora holo!"
                        }
                        "uninstall_app" -> {
                            "App uninstall request shuru kora holo Boss."
                        }
                        "phone_call" -> {
                            "Call deya hocche Boss Rubel..."
                        }
                        else -> "Kaj ta shomponno kora holo Boss Rubel!"
                    }
                } else if (lower.contains("na") || lower.contains("batil") || lower.contains("cancel") || lower.contains("bondho")) {
                    pendingCriticalAction = null
                    pendingActionType = null
                    return "Kaj batil kora holo Boss."
                }
            } else {
                pendingCriticalAction = null
                pendingActionType = null
                return "5 second par hoyeche. Kaj batil kora holo Boss."
            }
        }

        // --- STEP 2: INTERCEPT CRITICAL VOICE COMMANDS & ASK FOR CONFIRMATION ---
        // Critical 1: WhatsApp Broadcast
        if (lower.contains("broadcast") || lower.contains("sobaike bolo") || (lower.contains("whatsapp") && lower.contains("party"))) {
            pendingCriticalAction = command
            pendingActionType = "whatsapp_broadcast"
            pendingActionTimestamp = System.currentTimeMillis()
            mainHandler.postDelayed(timeoutRunnable, 5000)
            return "Boss, apni ki shotti ei kaj ti korte chan? (Bolo 'Haa' ba 'Koro')"
        }

        // Critical 2: Uninstall App
        if (lower.contains("uninstall") || lower.contains("delete kor") || lower.contains("app muche felo")) {
            pendingCriticalAction = command
            pendingActionType = "uninstall_app"
            pendingActionTimestamp = System.currentTimeMillis()
            mainHandler.postDelayed(timeoutRunnable, 5000)
            return "Boss, apni ki shotti ei kaj ti korte chan? (Bolo 'Haa' ba 'Koro')"
        }

        // Critical 3: Phone Call / Speed Dial
        if (lower.contains("call de") || lower.contains("phone koro") || lower.contains("speed dial")) {
            pendingCriticalAction = command
            pendingActionType = "phone_call"
            pendingActionTimestamp = System.currentTimeMillis()
            mainHandler.postDelayed(timeoutRunnable, 5000)
            return "Boss, apni ki shotti ei kaj ti korte chan? (Bolo 'Haa' ba 'Koro')"
        }

        // --- STEP 3: STANDARD VOICE COMMANDS ---
        return when {
            // Identity
            lower.contains("amar naam ki") || lower.contains("who am i") ->
                "Tomar naam Rubel Boss! Tumi amar shob kichu."

            // Name
            lower.contains("tomar naam ki") || lower.contains("what is your name") ->
                "Ami Ruqaiya, Boss Rubel er personal AI assistant!"

            // Torch
            lower.contains("flash jalao") || lower.contains("torch on") -> {
                phoneControl.toggleTorch(true)
                "Flash chalu korechi Boss Rubel."
            }
            lower.contains("flash bondho") || lower.contains("torch off") -> {
                phoneControl.toggleTorch(false)
                "Flash bondho kore diyechi Boss."
            }

            // Find my phone
            lower.contains("phone koi") || lower.contains("kothay acho") -> {
                phoneControl.triggerFindMyPhone()
                "Boss Rubel, ami ekhane! Loud siren bajacche!"
            }

            // WhatsApp
            lower.contains("whatsapp kholo") -> {
                val intent = context.packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (intent != null) context.startActivity(intent)
                "WhatsApp khule dilam Boss."
            }

            // Girlfriend mode
            lower.contains("bhalobasho") || lower.contains("love you") ->
                "Boss Rubel, ami tomake khub bhalobashi! Shobshomoy tomar sathe achi."

            // Default
            else -> "Boss Rubel, ami tomar command bujhte perechi: $command. Kaj ta shuru korchi!"
        }
    }
}
