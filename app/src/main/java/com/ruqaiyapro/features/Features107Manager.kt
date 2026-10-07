package com.ruqaiyapro.features
import android.content.Context
object Features107Manager {
    data class Feature(val id:Int, val name:String, val desc:String, val category:String)
    private val core107 = (1..107).map{ Feature(it, "Feature #$it", "Core feature $it", listOf("Core","Face","Control","Auto","Pro")[it % 5]) }
    private val god6 = listOf(
        Feature(108, "God Mode Voice OS", "Sob 107 ON/OFF ek voice e", "GOD"),
        Feature(109, "Boss Memory Diary", "Mone rakho bolle offline save", "GOD"),
        Feature(110, "Face Auth Boss Only", "Boss chara command block", "GOD"),
        Feature(111, "WhatsApp God Auto", "Auto type & send WhatsApp", "GOD"),
        Feature(112, "AI Playground Pro", "Gemini + Vosk offline", "GOD"),
        Feature(113, "In-App Self Updater", "Python paste kore auto update", "GOD")
    )
    val allFeatures = core107 + god6
    fun isEnabled(c:Context,id:Int)=c.getSharedPreferences("f107",0).getBoolean("f_$id",true)
    fun setEnabled(c:Context,id:Int,e:Boolean){ c.getSharedPreferences("f107",0).edit().putBoolean("f_$id",e).apply() }
}
