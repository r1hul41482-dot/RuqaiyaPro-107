package com.ruqaiyapro
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ruqaiyapro.service.RuqaiyaHotwordService
import com.ruqaiyapro.ui.screens.Features107Screen
import com.ruqaiyapro.ui.screens.MainDashboardScreen
import com.ruqaiyapro.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {
    private val perms = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ m-> if(m.values.all{it}) startHotword() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RuqaiyaProTheme { MainScreen() } }
    }
    private fun checkAndStart(){
        val ok = perms.all{ ContextCompat.checkSelfPermission(this,it)==PackageManager.PERMISSION_GRANTED }
        if(ok) startHotword() else launcher.launch(perms)
        if(!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
    }
    private fun startHotword(){
        val i = Intent(this, RuqaiyaHotwordService::class.java)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) startForegroundService(i) else startService(i)
    }
    private fun stopHotword(){ stopService(Intent(this, RuqaiyaHotwordService::class.java)) }

    @Composable
    fun MainScreen(){
        var sel by remember { mutableStateOf(0) }
        val tabs = listOf("Hub","Face","Control","Auto","107","Settings")
        Scaffold(bottomBar={
            NavigationBar{
                tabs.forEachIndexed{ i,t-> NavigationBarItem(selected=sel==i, onClick={sel=i}, icon={Text("${i+1}")}, label={Text(t)}) }
            }
        }){ pad->
            Box(Modifier.padding(pad).fillMaxSize()){
                when(sel){
                    0 -> MainDashboardScreen(onStartService={checkAndStart()}, onStopService={stopHotword()})
                    1 -> Box(Modifier.padding(16.dp)){ Text("Face - Boss Rubel Tracking") }
                    2 -> Box(Modifier.padding(16.dp)){ Text("Control - WiFi/BT/Flash/SOS") }
                    3 -> Box(Modifier.padding(16.dp)){ Text("Auto - WhatsApp Auto") }
                    4 -> Features107Screen()
                    5 -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun RuqaiyaProTheme(content:@Composable ()->Unit){
    MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFF06B6D4), secondary=Color(0xFFF43F5E), background=Color(0xFF0F172A), surface=Color(0xFF1E293B), onPrimary=Color.White, onBackground=Color(0xFFF8FAFC), onSurface=Color(0xFFF8FAFC)), content=content)
}
