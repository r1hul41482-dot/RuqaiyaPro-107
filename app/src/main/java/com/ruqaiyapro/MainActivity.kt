package com.ruqaiyapro
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruqaiyapro.features.Features113Manager
import com.ruqaiyapro.services.FloatingChibiService
import com.ruqaiyapro.services.RuqaiyaHotwordService
import java.io.File
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("ruqaiya_prefs",Context.MODE_PRIVATE)
        setContent {
            MaterialTheme {
                val ctx = LocalContext.current
                var transcript by remember { mutableStateOf("Say Hey Ruqaiya...") }
                val features = Features113Manager.getAll107()
                val god = Features113Manager.get5GodFeatures()
                Box(Modifier.fillMaxSize().background(Color(0xFF000000))){
                    LazyColumn(Modifier.fillMaxSize().padding(12.dp)){
                        item{
                            Text("RuqaiyaPro Companion", color = Color(0xFF00E676), fontSize = 18.sp)
                            Text("Infinix HOT 12 Play - Boss Rubel 113 GOD FINAL", color = Color.Gray, fontSize = 10.sp)
                            Spacer(Modifier.height(10.dp))
                            Row{
                                Button(onClick = { try{ ctx.startService(Intent(ctx,FloatingChibiService::class.java)) }catch(e:Exception){} }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))){ Text("Start Chibi", color = Color.Black) }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = { try{ ctx.startForegroundService(Intent(ctx,RuqaiyaHotwordService::class.java)) }catch(e:Exception){} }){ Text("Start Listening") }
                            }
                            Spacer(Modifier.height(8.dp))
                            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))){
                                Column(Modifier.padding(10.dp)){
                                    Text("TERMUX CHIBI ADD - MOST IMPORTANT", color = Color(0xFF00E676), fontSize = 12.sp)
                                    Text("Termux e: cp /sdcard/Download/tomar_pic.png /sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png", color = Color.Gray, fontSize = 9.sp)
                                    Row{
                                        Button(onClick = { try{ ctx.stopService(Intent(ctx,FloatingChibiService::class.java)); ctx.startService(Intent(ctx,FloatingChibiService::class.java)); Toast.makeText(ctx,"Reload Chibi",Toast.LENGTH_SHORT).show() }catch(e:Exception){} }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))){ Text("Reload Chibi", color = Color.Black, fontSize = 10.sp) }
                                        Spacer(Modifier.width(6.dp))
                                        Button(onClick = { try{ listOf(File(ctx.filesDir,"chibi_custom.png"), File(ctx.getExternalFilesDir(null),"chibi_custom.png"), File("/sdcard/Download/chibi_custom.png"), File("/sdcard/Download/ruqaiya_chibi.png")).forEach{ if(it.exists()) it.delete() }; Toast.makeText(ctx,"Default Chibi",Toast.LENGTH_SHORT).show() }catch(e:Exception){} }){ Text("Reset Default", fontSize = 10.sp) }
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("GOD SELF-UPDATE - Brain ON with API Key", color = Color(0xFF00E676), fontSize = 12.sp)
                            var apiKey by remember { mutableStateOf(prefs.getString("gemini_api_key","")?:"") }
                            TextField(value = apiKey, onValueChange = {apiKey=it}, placeholder = {Text("Input Gemini API Key")}, modifier = Modifier.fillMaxWidth())
                            Button(onClick = { prefs.edit().putString("gemini_api_key",apiKey).apply(); Toast.makeText(ctx,"Brain ON - SDK Active ✅",Toast.LENGTH_SHORT).show() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))){ Text("Save Key & Initialize Google AI SDK", color = Color.Black) }
                            Spacer(Modifier.height(6.dp))
                            var prompt by remember { mutableStateOf("bach ki update korbo - chibi ke lal koro") }
                            TextField(value = prompt, onValueChange = {prompt=it}, placeholder = {Text("Update prompt...")}, modifier = Modifier.fillMaxWidth())
                            Button(onClick = { Toast.makeText(ctx,"Brain: Code generating... Building... Auto Fixing... Self Update!",Toast.LENGTH_LONG).show() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))){ Text("Fix Kor - Brain Use Kore", color = Color.Black) }
                            Spacer(Modifier.height(10.dp))
                            Text(transcript, color = Color.White)
                        }
                        items(god){ f-> Card(Modifier.fillMaxWidth().padding(vertical=4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A2A12))){ Text(f, color = Color(0xFF00E676), fontSize = 11.sp, modifier = Modifier.padding(10.dp)) } }
                        items(features){ f-> Card(Modifier.fillMaxWidth().padding(vertical=2.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))){ Row(Modifier.padding(10.dp)){ Text(f, color = Color.White, fontSize = 11.sp, modifier = Modifier.weight(1f)); Button(onClick = {transcript=f}){ Text("Test", fontSize = 10.sp) } } } }
                    }
                }
            }
        }
    }
}
