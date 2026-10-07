package com.ruqaiyapro.system

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.widget.Toast
import kotlinx.coroutines.*

class PhoneControlManager(private val context: Context) {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var isTorchOn = false

    fun toggleTorch(enable: Boolean) {
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, enable)
            isTorchOn = enable
            Toast.makeText(context, if (enable) "Flash Chalu!" else "Flash Bondho!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Torch SOS Strobe (... --- ...)
    fun triggerSosStrobe(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            try {
                val cameraId = cameraManager.cameraIdList[0]
                for (i in 1..5) {
                    cameraManager.setTorchMode(cameraId, true)
                    delay(150)
                    cameraManager.setTorchMode(cameraId, false)
                    delay(150)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Find My Phone ("Phone koi?") Loud Siren
    fun triggerFindMyPhone() {
        try {
            // Set max volume
            val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, AudioManager.FLAG_SHOW_UI)

            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val player = MediaPlayer.create(context, alarmUri)
            player?.start()

            toggleTorch(true)
            Toast.makeText(context, "Boss Rubel, ami ekhane! Phone pawa geche!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
