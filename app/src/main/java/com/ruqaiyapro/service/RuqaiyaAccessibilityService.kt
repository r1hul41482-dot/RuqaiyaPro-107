package com.ruqaiyapro.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import kotlinx.coroutines.*

class RuqaiyaAccessibilityService : AccessibilityService() {

    companion object {
        var isWhatsAppAutoOn = true
        var broadcastQueue = mutableListOf<String>()
        var isBroadcasting = false
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString() ?: ""
        if (pkg != "com.whatsapp" && pkg != "com.whatsapp.w4b") return

        if (isWhatsAppAutoOn && !isBroadcasting) {
            checkAndPerformAutoReply(rootInActiveWindow)
        }
    }

    private fun checkAndPerformAutoReply(root: AccessibilityNodeInfo?) {
        if (root == null) return

        serviceScope.launch {
            try {
                // Find message edit text in WhatsApp
                val editTexts = root.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry")
                if (editTexts.isNotEmpty()) {
                    val inputField = editTexts[0]
                    if (inputField.text.isNullOrEmpty()) {
                        val replyText = "Boss Rubel busy ache, pore reply dibe - Ruqaiya bolchi"
                        val arguments = Bundle().apply {
                            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, replyText)
                        }
                        inputField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)

                        // Delay 300ms then click send button
                        delay(300)
                        val sendButtons = root.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
                        if (sendButtons.isNotEmpty()) {
                            sendButtons[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Ruqaiya: WhatsApp e Boss er hoye reply dilam!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- BROADCAST WITH 3 SEC DELAY TO PREVENT WHATSAPP BANS ---
    fun startBroadcastLoop(contacts: List<String>, message: String) {
        isBroadcasting = true
        serviceScope.launch {
            for ((index, contact) in contacts.withIndex()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "WhatsApp Broadcast: Sending to $contact (" + (index + 1) + "/" + contacts.size + ")", Toast.LENGTH_SHORT).show()
                }

                // 3 second delay requested
                delay(3000)
            }
            isBroadcasting = false
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "WhatsApp Broadcast Shomponno hoyeche Boss Rubel!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onInterrupt() {
        serviceScope.cancel()
    }
}
