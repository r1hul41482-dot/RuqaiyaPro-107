package com.ruqaiyapro.ai

import android.content.Context
import android.content.SharedPreferences
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AiSettingsManager for RuqaiyaPro Android.
 * Manages Gemini API Key storage, initialization of Google AI SDK (GenerativeModel),
 * and dynamic AI execution for Boss Rubel.
 */
class AiSettingsManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ruqaiyapro_ai_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GEMINI_API_KEY = "gemini_api_key"
        private const val KEY_AI_ENABLED = "ai_features_enabled"
        private const val MODEL_NAME = "gemini-3.8-flash"
    }

    private var generativeModel: GenerativeModel? = null

    init {
        initializeSdkIfConfigured()
    }

    fun saveGeminiApiKey(apiKey: String) {
        prefs.edit().putString(KEY_GEMINI_API_KEY, apiKey.trim()).apply()
        initializeSdkIfConfigured()
    }

    fun getGeminiApiKey(): String {
        return prefs.getString(KEY_GEMINI_API_KEY, "") ?: ""
    }

    fun setAiFeaturesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AI_ENABLED, enabled).apply()
    }

    fun isAiFeaturesEnabled(): Boolean {
        return prefs.getBoolean(KEY_AI_ENABLED, true)
    }

    fun isSdkInitialized(): Boolean {
        return generativeModel != null
    }

    fun initializeSdkIfConfigured(): Boolean {
        val key = getGeminiApiKey()
        return if (key.isNotEmpty()) {
            try {
                generativeModel = GenerativeModel(
                    modelName = MODEL_NAME,
                    apiKey = key
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                generativeModel = null
                false
            }
        } else {
            generativeModel = null
            false
        }
    }

    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        if (!isAiFeaturesEnabled()) {
            return@withContext "Boss Rubel, AI features bondho kora ache app settings e."
        }

        val model = generativeModel
        if (model == null) {
            return@withContext "Boss Rubel, Gemini API Key configure kora hoyni settings e. Settings e giye API key save korun."
        }

        try {
            val response = model.generateContent(
                "You are Ruqaiya, Boss Rubel's personal assistant. Reply affectionately in concise Banglish/Bengali: " + prompt
            )
            response.text ?: "Boss Rubel, ami Uttar peyechi kintu text khali chilo."
        } catch (e: Exception) {
            "Boss Rubel, AI request fail hoyeche: " + (e.localizedMessage ?: "Network error")
        }
    }
}
