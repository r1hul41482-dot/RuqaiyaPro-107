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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ruqaiyapro.service.RuqaiyaHotwordService
import com.ruqaiyapro.ui.screens.*

class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ perms->
        if(perms.values.all{it}) startHotwordService()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RuqaiyaProAppTheme { MainScreen(onStartService={checkAndStart()}, onStopService={stopHotwordService()}) } }
    }
    private fun checkAndStart(){
        val allGranted = permissionsToRequest.all{ ContextCompat.checkSelfPermission(this,it)==PackageManager.PERMISSION_GRANTED }
        if(allGranted){ startHotwordService() } else { permissionLauncher.launch(permissionsToRequest) }
        if(!Settings.canDrawOverlays(this)){
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }
    }
    private fun startHotwordService(){
        val intent = Intent(this, RuqaiyaHotwordService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
    }
    private fun stopHotwordService(){
        val intent = Intent(this, RuqaiyaHotwordService::class.java)
        stopService(intent)
    }
}

@Composable
fun MainScreen(onStartService:()->Unit, onStopService:()->Unit){
    var selected by remember { mutableStateOf(0) }
    val tabs = listOf("Hub","Face","Control","Auto","107","Settings")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Face, Icons.Filled.Settings, Icons.Filled.Send, Icons.Filled.List, Icons.Filled.Settings)
    Scaffold(bottomBar={
        NavigationBar{
            tabs.forEachIndexed{ i,t->
                NavigationBarItem(selected=selected==i, onClick={selected=i}, icon={Icon(icons[i], contentDescription=t)}, label={Text(t)})
            }
        }
    }){ pad->
        Box(Modifier.padding(pad).fillMaxSize()){
            when(selected){
                0 -> MainDashboardScreen(onStartService=onStartService, onStopService=onStopService)
                1 -> FaceScreen()
                2 -> ControlScreen()
                3 -> AutoScreen()
                4 -> Features107Screen()
                5 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun FaceScreen(){ Column(Modifier.fillMaxSize().padding(16.dp)){ Text("Face - Boss Rubel Face Tracking", style=MaterialTheme.typography.headlineSmall); Text("CameraX Preview + Face Detection") } }
@Composable
fun ControlScreen(){ Column(Modifier.fillMaxSize().padding(16.dp)){ Text("Control - WiFi/BT/Flash/SOS", style=MaterialTheme.typography.headlineSmall); Text("WiFi ON/OFF, Bluetooth, Flashlight, Find My Phone") } }
@Composable
fun AutoScreen(){ Column(Modifier.fillMaxSize().padding(16.dp)){ Text("Auto - WhatsApp Auto", style=MaterialTheme.typography.headlineSmall); Text("WhatsApp Auto-ON, Broadcast, Auto Reply") } }

@Composable
fun RuqaiyaProAppTheme(content: @Composable ()->Unit){
    MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFF06B6D4), secondary=Color(0xFFFF43F5E), background=Color(0xFF0F172A), surface=Color(0xFF1E293B), onPrimary=Color.White, onBackground=Color(0xFFF8FAFC), onSurface=Color(0xFFF8FAFC)), content=content)
}
