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
import com.ruqaiyapro.ui.screens.*

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
        val tabs = listOf("Hub","Face","Ctrl","Auto","107","Upd","Set")
        Scaffold(bottomBar={
            NavigationBar{
                tabs.forEachIndexed{ i,t-> NavigationBarItem(selected=sel==i, onClick={sel=i}, icon={Text(t.first().toString())}, label={Text(t)}) }
            }
        }){ pad->
            Box(Modifier.padding(pad).fillMaxSize()){
                when(sel){
                    0 -> MainDashboardScreen(onStartService={checkAndStart()}, onStopService={stopHotword()})
                    1 -> FaceScreenGod()
                    2 -> ControlGodScreen()
                    3 -> AutoGodScreen()
                    4 -> Features107Screen()
                    5 -> UpdateScreen()
                    6 -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun FaceScreenGod(){ Column(Modifier.padding(16.dp)){ Text("Face God - Boss Rubel Only Voice"); Text("If not Boss face, no command works") } }
@Composable
fun ControlGodScreen(){ Column(Modifier.padding(16.dp)){ Text("Control God Mode - All ON/OFF"); Button(onClick={}){Text("God Mode ON - All 107 ON")} } }
@Composable
fun AutoGodScreen(){ Column(Modifier.padding(16.dp)){ Text("Auto God - WhatsApp Auto Send") } }

@Composable
fun RuqaiyaProTheme(content:@Composable ()->Unit){
    MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFF06B6D4), secondary=Color(0xFFF43F5E), background=Color(0xFF0F172A), surface=Color(0xFF1E293B), onPrimary=Color.White, onBackground=Color(0xFFF8FAFC), onSurface=Color(0xFFF8FAFC)), content=content)
}
