package com.ruqaiyapro.features
import android.content.Context
object FaceAuthManager {
    fun isBossFaceDetected(ctx: Context)=ctx.getSharedPreferences("face_auth",0).getBoolean("boss_detected",true) // stub true for now, later CameraX MLKit
    fun setBossDetected(ctx: Context, v: Boolean){ ctx.getSharedPreferences("face_auth",0).edit().putBoolean("boss_detected",v).apply() }
}
