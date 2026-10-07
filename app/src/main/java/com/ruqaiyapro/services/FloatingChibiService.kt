package com.ruqaiyapro.services
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import java.io.File
class FloatingChibiService : Service() {
    private var windowManager: WindowManager? = null
    private var chibiView: ImageView? = null
    private var params: WindowManager.LayoutParams? = null
    private var initialX = 0; private var initialY = 0; private var initialTouchX = 0f; private var initialTouchY = 0f
    private var scaleDetector: ScaleGestureDetector? = null
    fun loadChibiBitmap(): android.graphics.Bitmap? {
        try {
            val paths = listOf(File(filesDir,"chibi_custom.png"), File(getExternalFilesDir(null),"chibi_custom.png"), File("/sdcard/Download/chibi_custom.png"), File("/sdcard/Download/ruqaiya_chibi.png"), File("/sdcard/Download/chibi.png"))
            for(f in paths){ if(f.exists()){ return BitmapFactory.decodeFile(f.absolutePath) } }
        }catch(e:Exception){}
        return null
    }
    override fun onCreate() {
        super.onCreate()
        try {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            chibiView = ImageView(this)
            val custom = loadChibiBitmap()
            if(custom!=null){ chibiView?.setImageBitmap(custom); Toast.makeText(this,"Ruqaiya: Custom Chibi loaded 😍",Toast.LENGTH_SHORT).show() }
            else{ chibiView?.setImageResource(android.R.mipmap.sym_def_app_icon); Toast.makeText(this,"Ruqaiya: Default Chibi",Toast.LENGTH_SHORT).show() }
            val prefs = getSharedPreferences("ruqaiya_prefs",Context.MODE_PRIVATE)
            val sx = prefs.getInt("chibi_x",100); val sy = prefs.getInt("chibi_y",300); val ss = prefs.getInt("chibi_size",120)
            params = WindowManager.LayoutParams(ss,ss,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT)
            params?.x=sx; params?.y=sy; params?.gravity=Gravity.TOP or Gravity.START
            scaleDetector = ScaleGestureDetector(this, object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
                override fun onScale(d: ScaleGestureDetector): Boolean {
                    try{ val f=d.scaleFactor; var ns=((params?.width?:120)*f).toInt(); val min=(40*resources.displayMetrics.density).toInt(); val max=(300*resources.displayMetrics.density).toInt(); ns=ns.coerceIn(min,max); params?.width=ns; params?.height=ns; windowManager?.updateViewLayout(chibiView,params); prefs.edit().putInt("chibi_size",ns).apply(); Toast.makeText(this@FloatingChibiService,"Chibi size: ${ns}dp",Toast.LENGTH_SHORT).show() }catch(e:Exception){}
                    return true
                }
            })
            chibiView?.setOnTouchListener { v, event ->
                try{ scaleDetector?.onTouchEvent(event); if(event.pointerCount==1){ when(event.action){ MotionEvent.ACTION_DOWN->{initialX=params?.x?:0; initialY=params?.y?:0; initialTouchX=event.rawX; initialTouchY=event.rawY} MotionEvent.ACTION_MOVE->{if(event.pointerCount==1){params?.x=initialX+(event.rawX-initialTouchX).toInt(); params?.y=initialY+(event.rawY-initialTouchY).toInt(); windowManager?.updateViewLayout(chibiView,params)}} MotionEvent.ACTION_UP->{prefs.edit().putInt("chibi_x",params?.x?:0).putInt("chibi_y",params?.y?:0).apply(); v.performClick()}}} }catch(e:SecurityException){Toast.makeText(this,"XOS overlay blocked",Toast.LENGTH_SHORT).show()}catch(e:Exception){}
                true
            }
            chibiView?.setOnClickListener { Toast.makeText(this,"🎤 Ji Rubel Boss bolo...",Toast.LENGTH_SHORT).show() }
            chibiView?.setOnLongClickListener { Toast.makeText(this,"Chibi moved, pinched, long press reset",Toast.LENGTH_SHORT).show(); true }
            windowManager?.addView(chibiView,params)
        }catch(e:Exception){ Toast.makeText(this,"Chibi error: ${e.message}",Toast.LENGTH_SHORT).show() }
    }
    override fun onDestroy() { super.onDestroy(); try{ if(chibiView!=null) windowManager?.removeView(chibiView) }catch(e:Exception){} }
    override fun onBind(intent: Intent?): IBinder? = null
}
