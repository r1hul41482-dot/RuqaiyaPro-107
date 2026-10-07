package com.ruqaiyapro.services
import android.app.*
import android.content.*
import android.media.AudioManager
import android.os.*
import android.speech.*
import android.widget.Toast
import java.util.*
class RuqaiyaHotwordService: Service() {
    private var sr: SpeechRecognizer? = null
    private var am: AudioManager? = null
    private val h = Handler(Looper.getMainLooper())
    override fun onCreate() {
        super.onCreate()
        try{
            am = getSystemService(AUDIO_SERVICE) as AudioManager
            val ch = NotificationChannel("ruqaiya","Ruqaiya",NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NotificationManager::class.java)).createNotificationChannel(ch)
            val n = Notification.Builder(this,"ruqaiya").setContentTitle("Ruqaiya ghumacche... Bolo Hey Ruqaiya - Boss Rubel").setSmallIcon(android.R.mipmap.sym_def_app_icon).build()
            startForeground(101,n)
            startListen()
        }catch(e:Exception){}
    }
    fun startListen(){
        try{
            try{ am?.setStreamMute(AudioManager.STREAM_MUSIC,true); am?.setStreamMute(AudioManager.STREAM_SYSTEM,true); am?.setStreamMute(AudioManager.STREAM_NOTIFICATION,true) }catch(e:Exception){}
            h.postDelayed({
                try{
                    sr?.destroy(); sr = SpeechRecognizer.createSpeechRecognizer(this)
                    sr?.setRecognitionListener(object: RecognitionListener{
                        override fun onReadyForSpeech(p:Bundle?){ try{Toast.makeText(this@RuqaiyaHotwordService,"Tumi bolcho:",Toast.LENGTH_SHORT).show()}catch(e:Exception){} }
                        override fun onBeginningOfSpeech(){}
                        override fun onRmsChanged(v:Float){}
                        override fun onBufferReceived(b:ByteArray?){}
                        override fun onEndOfSpeech(){}
                        override fun onError(e:Int){ try{am?.setStreamMute(AudioManager.STREAM_MUSIC,false)}catch(ex:Exception){}; h.postDelayed({startListen()},1500) }
                        override fun onResults(b:Bundle?){
                            try{
                                val txt = b?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.lowercase()?:""
                                if(txt.contains("hey ruqaiya")||txt.contains("hey janu")||txt.contains("rubel boss")){
                                    Toast.makeText(this@RuqaiyaHotwordService,"🎤 Ji Rubel Boss bolo... Mone ache Boss!",Toast.LENGTH_SHORT).show()
                                    sendBroadcast(Intent("RUQAIYA_WAKE"))
                                }
                                am?.setStreamMute(AudioManager.STREAM_MUSIC,false)
                                h.postDelayed({startListen()},1000)
                            }catch(e:Exception){ h.postDelayed({startListen()},1500) }
                        }
                        override fun onPartialResults(p:Bundle?){}
                        override fun onEvent(a:Int,b:Bundle?){}
                    })
                    val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{ putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true) }
                    sr?.startListening(i)
                }catch(e:Exception){ h.postDelayed({startListen()},1500) }
            },500)
        }catch(e:Exception){}
    }
    override fun onStartCommand(i:Intent?,f:Int,s:Int)=START_STICKY
    override fun onBind(i:Intent?):IBinder? = null
}
