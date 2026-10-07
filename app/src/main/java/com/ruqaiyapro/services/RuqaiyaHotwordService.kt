package com.ruqaiyapro.services
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.*
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*
class RuqaiyaHotwordService : Service(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null; private var audioManager: AudioManager? = null; private val handler = Handler(Looper.getMainLooper())
    override fun onCreate() { super.onCreate(); try{ audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager; tts=TextToSpeech(this,this); startForegroundNotification(); startNoBeepListening() }catch(e:Exception){} }
    private fun startForegroundNotification(){ try{ val cid="ruqaiya_sleep"; if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){ val ch=NotificationChannel(cid,"Ruqaiya Sleep",NotificationManager.IMPORTANCE_LOW); (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)}; val n=NotificationCompat.Builder(this,cid).setContentTitle("Ruqaiya ghumacche... Bolo Hey Ruqaiya").setSmallIcon(android.R.mipmap.sym_def_app_icon).setOngoing(true).build(); startForeground(1,n) }catch(e:Exception){} }
    fun startNoBeepListening(){ try{ muteAll(true); handler.postDelayed({ try{ Toast.makeText(this,"Tumi bolcho: Listening...",Toast.LENGTH_SHORT).show() }catch(e:Exception){ unMuteAndRestart() } },500) }catch(e:Exception){} }
    private fun muteAll(m:Boolean){ try{ audioManager?.let{ am-> if(m){ am.setStreamMute(AudioManager.STREAM_MUSIC,true); am.setStreamMute(AudioManager.STREAM_SYSTEM,true); am.setStreamMute(AudioManager.STREAM_NOTIFICATION,true); am.setStreamMute(AudioManager.STREAM_ALARM,true); am.setStreamMute(AudioManager.STREAM_RING,true); am.setStreamMute(AudioManager.STREAM_DTMF,true) }else{ am.setStreamMute(AudioManager.STREAM_MUSIC,false); am.setStreamMute(AudioManager.STREAM_SYSTEM,false); am.setStreamMute(AudioManager.STREAM_NOTIFICATION,false); am.setStreamMute(AudioManager.STREAM_ALARM,false); am.setStreamMute(AudioManager.STREAM_RING,false); am.setStreamMute(AudioManager.STREAM_DTMF,false) } } }catch(e:Exception){} }
    private fun unMuteAndRestart(){ try{ muteAll(false); handler.postDelayed({ startNoBeepListening() },1500) }catch(e:Exception){} }
    fun onWakeWordDetected(){ try{ Toast.makeText(this,"🎤 Ji Rubel Boss bolo...",Toast.LENGTH_SHORT).show(); tts?.setPitch(1.15f); tts?.speak("Ji Rubel Boss bolo",TextToSpeech.QUEUE_FLUSH,null,"ruqaiya"); handler.postDelayed({ Toast.makeText(this,"Ruqaiya ghumacche...",Toast.LENGTH_SHORT).show(); startNoBeepListening() },4000) }catch(e:Exception){} }
    override fun onInit(s:Int){ try{ if(s==TextToSpeech.SUCCESS) tts?.language=Locale("bn","BD") }catch(e:Exception){} }
    override fun onBind(i:Intent?):IBinder?=null; override fun onDestroy(){ try{ tts?.shutdown() }catch(e:Exception){}; super.onDestroy() }
}
