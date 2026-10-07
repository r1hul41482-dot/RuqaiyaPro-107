package com.ruqaiyapro.ai
import android.content.Context
class AiSettingsManager(private val context: Context) {
    fun getRuqaiyaResponse(prompt: String): String {
        return "Hey! RuqaiyaPro 107 - Boss Rubel! You said: $prompt"
    }
    fun isAiEnabled(): Boolean = true
}
