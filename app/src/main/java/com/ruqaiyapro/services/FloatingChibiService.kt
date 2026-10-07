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
class FloatingChibiService: Service() {
    private var windowManager: WindowManager? = null
    private var chibiView: ImageView? = null
    private var params: WindowManager.LayoutParams? = null
    private val prefs by lazy { getSharedPreferences("ruqaiya_prefs",0) }
    fun loadChibiBitmap(): android.graphics.Bitmap? {
        try{
            val paths = listOf(
                File(filesDir, "chibi_custom.png"),
                File(getExternalFilesDir(null), "chibi_custom.png"),
                File("/sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png"),
                File("/sdcard/Download/chibi_custom.png"),
                File("/sdcard/Download/ruqaiya_chibi.png"),
                File("/sdcard/Download/chibi.png")
            )
            for(f in paths){ if(f.exists()){ return BitmapFactory.decodeFile(f.absolutePath) } }
        }catch(e:Exception){}
        return null
    }
    override fun onCreate() {
        super.onCreate()
        try{
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            chibiView = ImageView(this)
            val bmp = loadChibiBitmap()
            if(bmp!=null){ chibiView?.setImageBitmap(bmp); Toast.makeText(this,"Ruqaiya: Custom Chibi loaded 😍",Toast.LENGTH_SHORT).show() }
            else { chibiView?.setImageResource(android.R.mipmap.sym_def_app_icon); Toast.makeText(this,"Ruqaiya: Default Chibi",Toast.LENGTH_SHORT).show() }
            val size = prefs.getInt("chibi_size",120)
            params = WindowManager.LayoutParams(size,size,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT)
            params?.gravity = Gravity.TOP or Gravity.START
            params?.x = prefs.getInt("chibi_x",100); params?.y = prefs.getInt("chibi_y",300)
            var initX=0; var initY=0; var touchX=0f; var touchY=0f
            chibiView?.setOnTouchListener { _, ev ->
                try{
                    when(ev.action){
                        MotionEvent.ACTION_DOWN -> { initX=params!!.x; initY=params!!.y; touchX=ev.rawX; touchY=ev.rawY }
                        MotionEvent.ACTION_MOVE -> { if(ev.pointerCount==1){ params!!.x=initX+(ev.rawX-touchX).toInt(); params!!.y=initY+(ev.rawY-touchY).toInt(); windowManager?.updateViewLayout(chibiView,params) } }
                        MotionEvent.ACTION_UP -> { prefs.edit().putInt("chibi_x",params!!.x).putInt("chibi_y",params!!.y).apply() }
                    }
                }catch(e:Exception){}
                true
            }
            windowManager?.addView(chibiView,params)
        }catch(e:Exception){}
    }
    override fun onBind(i: Intent?): IBinder? = null
    override fun onDestroy() { try{ windowManager?.removeView(chibiView)}catch(e:Exception){}; super.onDestroy() }
    companion object { fun deleteAll(context: Context){ try{ listOf(File(context.filesDir,"chibi_custom.png"), File(context.getExternalFilesDir(null),"chibi_custom.png"), File("/sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png"), File("/sdcard/Download/chibi_custom.png")).forEach{ if(it.exists()) it.delete() } }catch(e:Exception){} } }
}
