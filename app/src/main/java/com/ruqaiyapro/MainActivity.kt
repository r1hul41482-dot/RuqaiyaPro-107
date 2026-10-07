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
import com.ruqaiyapro.ui.screens.MainDashboardScreen
import com.ruqaiyapro.ui.screens.SettingsScreen
import com.ruqaiyapro.ui.screens.Features107Screen

class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ perms-> if(perms.values.all{it}) startHotwordService() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RuqaiyaProAppTheme { MainScreen(onStartService={checkAndStart()}, onStopService={stopHotwordService()}) } }
    }
    private fun checkAndStart(){
        val allGranted = permissionsToRequest.all{ ContextCompat.checkSelfPermission(this,it)==PackageManager.PERMISSION_GRANTED }
        if(allGranted) startHotwordService() else permissionLauncher.launch(permissionsToRequest)
        if(!Settings.canDrawOverlays(this)){
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }
    }
    private fun startHotwordService(){
        val intent = Intent(this, RuqaiyaHotwordService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
    }
    private fun stopHotwordService(){ stopService(Intent(this, RuqaiyaHotwordService::class.java)) }
}

@Composable
fun MainScreen(onStartService:()->Unit, onStopService:()->Unit){
    var selected by remember { mutableStateOf(0) }
    val tabs = listOf("Hub","Face","Control","Auto","107","Settings")
    Scaffold(bottomBar={
        NavigationBar{
            tabs.forEachIndexed{ i,t->
                NavigationBarItem(
                    selected=selected==i,
                    onClick={selected=i},
                    icon={
                        when(i){
                            0->Icon(Icons.Filled.Home, t)
                            1->Icon(Icons.Filled.Face, t)
                            2->Icon(Icons.Filled.Settings, t)
                            3->Icon(Icons.Filled.Send, t)
                            4->Icon(Icons.Filled.List, t)
                            else->Icon(Icons.Filled.Person, t)
                        }
                    },
                    label={Text(t)}
                )
            }
        }
    }){ pad->
        Box(Modifier.padding(pad).fillMaxSize()){
            when(selected){
                0 -> MainDashboardScreen(onStartService=onStartService, onStopService=onStopService)
                1 -> Box(Modifier.fillMaxSize().padding(16.dp)){ Text("Face - Boss Rubel Tracking") }
                2 -> Box(Modifier.fillMaxSize().padding(16.dp)){ Text("Control - WiFi/BT/Flash/SOS") }
                3 -> Box(Modifier.fillMaxSize().padding(16.dp)){ Text("Auto - WhatsApp Auto") }
                4 -> Features107Screen()
                5 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun RuqaiyaProAppTheme(content: @Composable ()->Unit){
    MaterialTheme(
        colorScheme=darkColorScheme(
            primary=Color(0xFF06B6D4),
            secondary=Color(0xFFF43F5E),
            background=Color(0xFF0F172A),
            surface=Color(0xFF1E293B),
            onPrimary=Color.White,
            onBackground=Color(0xFFF8FAFC),
            onSurface=Color(0xFFF8FAFC)
        ),
        content=content
    )
}
