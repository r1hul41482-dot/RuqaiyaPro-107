package com.ruqaiyapro.features
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.provider.Settings
object GodModeManager {
    fun enableAll(ctx: Context){
        for(i in 1..107) Features107Manager.setEnabled(ctx,i,true)
        try{ (ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = true }catch(_:Exception){}
        try{ (ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0) }catch(_:Exception){}
        ctx.getSharedPreferences("god_mode",0).edit().putBoolean("god_on",true).apply()
    }
    fun disableAll(ctx: Context){
        for(i in 1..107) Features107Manager.setEnabled(ctx,i,false)
        ctx.getSharedPreferences("god_mode",0).edit().putBoolean("god_on",false).apply()
    }
    fun isGodOn(ctx: Context)=ctx.getSharedPreferences("god_mode",0).getBoolean("god_on",false)
}
