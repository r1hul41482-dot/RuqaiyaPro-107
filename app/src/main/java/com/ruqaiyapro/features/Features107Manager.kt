package com.ruqaiyapro.features
import android.content.Context
object Features107Manager {
    data class Feature(val id:Int, val name:String, val desc:String, val category:String)
    val allFeatures = (1..107).map{ Feature(it, "Feature #$it", "Description for $it", listOf("Core","Face","Control","Auto","Pro")[it % 5]) }
    fun isEnabled(c:Context,id:Int)=c.getSharedPreferences("f107",0).getBoolean("f_$id",true)
    fun setEnabled(c:Context,id:Int,e:Boolean){ c.getSharedPreferences("f107",0).edit().putBoolean("f_$id",e).apply() }
}
