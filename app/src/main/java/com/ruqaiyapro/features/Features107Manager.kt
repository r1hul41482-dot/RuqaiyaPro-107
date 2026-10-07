package com.ruqaiyapro.features
import android.content.Context
object Features107Manager {
    data class Feature(val id: Int, val name: String, val desc: String, val category: String, val voice: String)
    val allFeatures = listOf(
        Feature(1, "Offline Vosk", "Net chara voice", "Core", "Offline e shuno"),
        Feature(2, "Battery 1% Saver", "1% e sob off", "Core", "Battery bachao"),
        Feature(3, "Screen Off Listening", "Screen off e sunbe", "Core", "Screen bondho te shuno"),
        Feature(4, "No-Sleep Trick", "XOS e marbe na", "Core", "No sleep"),
        Feature(5, "Hey Ruqaiya Hotword", "Hey Ruqaiya bolle wake", "Core", "Hey Ruqaiya"),
        Feature(6, "Quick Triggers", "Phone koi, Flash jalao", "Core", "Phone koi"),
        Feature(7, "Boss Memory", "Rubel ke mone rakhe", "Core", "Amar naam ki"),
        Feature(8, "Sweet Female TTS", "Misti voice", "Core", "Misti voice"),
        Feature(9, "Multi-AI Brain", "Gemini/OpenAI/Claude/Grok", "Core", "Brain Online"),
        Feature(16, "Boss Rubel Face Tracking", "Camera te chine", "Face", "Amake chinte perecho"),
        Feature(26, "Bangla Roast", "Amake roast koro", "Face", "Amake roast koro"),
        Feature(27, "Shayari", "Shayari bolo", "Face", "Shayari bolo"),
        Feature(28, "Koutuk", "Koutuk bolo", "Face", "Koutuk bolo"),
        Feature(30, "Torch On/Off Voice", "Flash jalao/bondho", "Control", "Flash jalao"),
        Feature(61, "WiFi ON/OFF", "WiFi control", "Control", "WiFi on"),
        Feature(62, "Bluetooth ON/OFF", "BT control", "Control", "Bluetooth on"),
        Feature(66, "Find My Phone", "Phone koi siren", "Control", "Phone koi"),
        Feature(71, "RAM Clean", "1 tap clean", "Control", "RAM clean"),
        Feature(81, "WhatsApp Auto-ON", "Auto type", "Auto", "WhatsApp Auto ON"),
        Feature(82, "WhatsApp Broadcast", "5 jon ke", "Auto", "WhatsApp e sobaike bolo"),
        Feature(84, "Auto Reply busy", "Boss busy", "Auto", "Busy ache"),
        Feature(90, "In-App Code Help", "Logcat fix", "Auto", "Auto Fix"),
        Feature(96, "Live AI Playground", "Gemini 1.5 Flash", "Pro", "Playground"),
        Feature(102, "All 107 ON/OFF Toggle", "Sob toggle", "Pro", "God Mode ON"),
        Feature(107, "Boss Rubel God Mode", "Sob ON/OFF", "Pro", "God Mode ON")
    )
    fun isEnabled(ctx: Context, id: Int) = ctx.getSharedPreferences("ruqaiya_features",0).getBoolean("feature_$id", true)
    fun setEnabled(ctx: Context, id: Int, en: Boolean){ ctx.getSharedPreferences("ruqaiya_features",0).edit().putBoolean("feature_$id", en).apply() }
}
