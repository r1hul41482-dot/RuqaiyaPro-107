package com.ruqaiyapro.features
import android.content.Context
object Features113Manager {
    data class Feature(val id:Int, val name:String, val desc:String, val cat:String, val bn:String)
    val all = (1..15).map{
        when(it){
            1->Feature(1,"Offline Vosk Recognition","No internet voice","Core","Offline e shuno")
            2->Feature(2,"Battery 1% Saver Mode","Sensor 0.9Hz, screen 1%","Core","Battery 1%")
            3->Feature(3,"Screen Off Listening","Wake lock background","Core","Screen bondho e shuno")
            4->Feature(4,"Boss Face Memory","Amake Mone Rakho - faces/rubel.jpg","Face","Amake Mone Rakho Rubel")
            5->Feature(5,"Face God Boss Only","Boss chara no command","Face","Face God")
            6->Feature(6,"Auto Blur Privacy","Box blur","Core","Auto Blur")
            7->Feature(7,"WhatsApp Auto Reply","Boss busy ache","Auto","WhatsApp Auto")
            8->Feature(8,"WhatsApp Broadcast Anti-Ban","3 sec delay","Auto","Broadcast")
            9->Feature(9,"WiFi Control","WiFi ON/OFF","Control","WiFi")
            10->Feature(10,"Bluetooth Control","BT ON/OFF","Control","Bluetooth")
            11->Feature(11,"Flashlight","Torch","Control","Flashlight")
            12->Feature(12,"Volume 65%","Master Volume","Control","Volume")
            13->Feature(13,"Brightness 70%","Screen Brightness","Control","Brightness")
            14->Feature(14,"Find My Phone Siren","Phone Koi loud siren","Control","Phone Koi")
            else->Feature(15,"RAM Clean Tuner","Infinix RAM clean","Control","RAM Clean")
        }
    } + (16..107).map{ Feature(it,"Pro Feature #$it","Infinix HOT 12 optimized #$it","Pro","Feature $it") } + listOf(
        Feature(108,"God Mode Voice OS","All 107 ON/OFF","GOD","God Mode"),
        Feature(109,"Boss Memory Diary - Rubel","Mone rakho offline diary - Boss Rubel ke mone rakhe","GOD","Memory Diary - Rubel Boss"),
        Feature(110,"Face Auth Boss Only","Boss Rubel chara block","GOD","Face Auth Rubel"),
        Feature(111,"WhatsApp God Auto","Auto type send","GOD","WhatsApp God"),
        Feature(112,"AI Playground Pro - Brain ON","Gemini/OpenAI any API = Brain ON","GOD","Brain ON - Any AI API"),
        Feature(113,"Self Updater Chibi","Python + Chibi Termux cp","GOD","Self Updater + Chibi")
    )
    fun isEnabled(c:Context,id:Int)=try{c.getSharedPreferences("f113",0).getBoolean("f_$id",true)}catch(e:Exception){true}
    fun setEnabled(c:Context,id:Int,e:Boolean){try{c.getSharedPreferences("f113",0).edit().putBoolean("f_$id",e).apply()}catch(e:Exception){}}
    fun allOn(c:Context,on:Boolean){try{val ed=c.getSharedPreferences("f113",0).edit(); all.forEach{ed.putBoolean("f_${it.id}",on)}; ed.apply()}catch(e:Exception){}}
}
