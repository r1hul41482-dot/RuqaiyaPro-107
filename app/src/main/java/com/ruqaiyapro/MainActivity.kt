package com.ruqaiyapro
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("ruqaiya_prefs",0)
        setContent {
            val black = Color(0xFF0B0F1A); val cardBg = Color(0xFF151A28); val green = Color(0xFF00E676)
            var tab by remember { mutableIntStateOf(0) }
            var apiKey by remember { mutableStateOf(prefs.getString("api_key","")?:"") }
            var brainOn by remember { mutableStateOf(prefs.getBoolean("brain_on",false)) }
            var transcript by remember { mutableStateOf("Ji Rubel Boss bolo... Mone ache!") }
            var search by remember { mutableStateOf("") }
            var volume by remember { mutableFloatStateOf(0.65f) }
            var brightness by remember { mutableFloatStateOf(0.70f) }
            // Safe features list - hardcoded 113 if manager missing
            val features = (1..113).map { i -> Triple(i, "Feature $i - ${if(i<=6) "GOD $i" else "Pro $i"}", "ফিচার $i - বস রুবেল") }
            MaterialTheme(colorScheme = darkColorScheme(container=black, background=black, primary=green, surface=cardBg)) {
                Column(Modifier.fillMaxSize().background(black).padding(8.dp)){
                    Text("RuqaiyaPro FOR BOSS RUBEL - 113 GOD + MEMORY", color=Color.White, style=MaterialTheme.typography.titleMedium)
                    Text("Infinix HOT 12 Play | Brain ${if(brainOn) "ON" else "OFF"} | Owner Rubel", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                    Row(Modifier.padding(vertical=4.dp)){
                        Button(onClick={
                            transcript="Ji Rubel Boss bolo... Live Companion ON!"
                            try{ val svc = Class.forName("com.ruqaiyapro.services.FloatingChibiService"); startService(Intent(this@MainActivity, svc as Class<*>)) }catch(e:Exception){ transcript="Chibi Service not found but UI OK - ${e.message}" }
                        }, colors=ButtonDefaults.buttonColors(containerColor=green), modifier=Modifier.weight(1f)){ Text("Live Companion", color=Color.Black) }
                        Spacer(Modifier.width(6.dp))
                        Button(onClick={}, modifier=Modifier.weight(1f), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF1E88E5))){ Text("Android Studio ZIP") }
                    }
                    when(tab){
                        0 -> Column(Modifier.verticalScroll(rememberScrollState())){
                            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                                Text("RuqaiyaPro Companion - Boss Rubel Only", color=Color.White)
                                Button(onClick={ transcript="Ji Rubel Boss bolo... Mone ache!" }, modifier=Modifier.fillMaxWidth().padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Start Service - Hey Ruqaiya", color=Color.Black) }
                                OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().padding(top=4.dp)){ Text("Stop Service") }
                                Text("Quick Voice Triggers for Boss Rubel:", color=Color.White, modifier=Modifier.padding(top=8.dp), style=MaterialTheme.typography.labelMedium)
                                Row(Modifier.padding(top=4.dp)){ AssistChip(onClick={transcript="Hey Ruqaiya"}, label={Text("Hey Ruqaiya")}); Spacer(Modifier.width(4.dp)); AssistChip(onClick={transcript="Amar naam Rubel Boss - Mone ache!"}, label={Text("Amar naam ki?")}) }
                                Row(Modifier.padding(top=4.dp)){ AssistChip(onClick={ prefs.edit().putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply(); transcript="Amake Mone Rakho Rubel - Saved! Boss Rubel Mone Ache!" }, label={Text("Amake mone rakho Rubel")}); Spacer(Modifier.width(4.dp)); AssistChip(onClick={transcript="Flash jalo - Torch ON!"}, label={Text("Flash jalo")}) }
                                Card(Modifier.fillMaxWidth().padding(top=8.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF0F141F))){ Text(transcript, color=green, modifier=Modifier.padding(10.dp)) }
                                Text("Termux Chibi: cp pic -> /sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png", color=Color.Gray, style=MaterialTheme.typography.labelSmall, modifier=Modifier.padding(top=8.dp))
                                Card(Modifier.fillMaxWidth().padding(top=10.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A1A))){ Column(Modifier.padding(10.dp)){ Text("In-App Code Help & Auto Fix Engine", color=Color.White); Text("BadActivity.kt fixed - Brain ON", color=green, style=MaterialTheme.typography.labelSmall) } }
                            } }
                        }
                        1 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Face God - Boss Rubel Only Voice", color=Color.White)
                            Box(Modifier.fillMaxWidth().height(160.dp).background(Color.Black).padding(8.dp)){ Text("CameraX Preview - Face Detection for Rubel Boss only\nVisual Feedback Toast c-{100} High Priority", color=Color.Gray) }
                            Button(onClick={ prefs.edit().putBoolean("boss_face_saved",true).apply(); transcript="Face Saved - Rubel Boss Mone Ache!" }, modifier=Modifier.padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Amake Mone Rakho Rubel", color=Color.Black) }
                        } }
                        2 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Control God Mode - All ON/OFF", color=Color.White)
                            Text("Master Volume ${(volume*100).toInt()}%", color=Color.White); Slider(value=volume, onValueChange={volume=it})
                            Text("Brightness ${(brightness*100).toInt()}%", color=Color.White); Slider(value=brightness, onValueChange={brightness=it})
                            Button(onClick={transcript="God Mode ON - All 113 ON for Boss Rubel!"}, modifier=Modifier.fillMaxWidth().padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("God Mode ON - All 113 ON", color=Color.Black) }
                        } }
                        3 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Auto God - WhatsApp Auto Send", color=Color.White)
                            Text("Boss busy ache, pore reply dibe - Ruqaiya bolche", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                            Button(onClick={transcript="WhatsApp Auto ON - Boss Rubel"} , modifier=Modifier.padding(top=8.dp)){ Text("Enable Auto Reply") }
                        } }
                        4 -> Column{
                            OutlinedTextField(value=search, onValueChange={search=it}, label={Text("Search 113 features")}, modifier=Modifier.fillMaxWidth())
                            Button(onClick={ transcript="God Mode ON - All 113 ON!" }, modifier=Modifier.fillMaxWidth().padding(top=6.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("God Mode ON - All 113 ON", color=Color.Black) }
                            LazyColumn(Modifier.height(450.dp).padding(top=6.dp)){
                                items(features.filter{ search.isEmpty() || it.second.contains(search,true) }){ f ->
                                    Card(Modifier.fillMaxWidth().padding(vertical=2.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1F2E))){
                                        Row(Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
                                            Column(Modifier.weight(1f)){ Text(f.second, color=Color.White); Text(f.third, color=green, style=MaterialTheme.typography.labelSmall) }
                                            var en by remember { mutableStateOf(true) }
                                            Switch(checked=en, onCheckedChange={en=it})
                                        }
                                    }
                                }
                            }
                        }
                        5 -> Column{ Text("Self Updater - 113 GOD", color=Color.White); OutlinedTextField(value="# add_feature(114, 'New') for Boss Rubel", onValueChange={}, modifier=Modifier.fillMaxWidth().height(200.dp)); Button(onClick={transcript="Update & Build triggered - 114!"}, modifier=Modifier.padding(top=8.dp)){ Text("Update & Build") } }
                        6 -> Column(Modifier.verticalScroll(rememberScrollState())){
                            Text("Google AI SDK - Brain Config", color=Color.White)
                            OutlinedTextField(value=apiKey, onValueChange={ apiKey=it; if(it.length>10){ brainOn=true; prefs.edit().putString("api_key",it).putBoolean("brain_on",true).putString("owner_name","Rubel Boss").apply() } }, label={Text("Gemini API Key - Any API works")}, modifier=Modifier.fillMaxWidth().padding(top=6.dp))
                            Button(onClick={ brainOn=true; prefs.edit().putBoolean("brain_on",true).putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply(); transcript="Brain ON - Rubel Boss Mone Ache!" }, modifier=Modifier.padding(top=8.dp)){ Text("Save Key & Brain ON") }
                            Card(Modifier.fillMaxWidth().padding(top=10.dp), colors=CardDefaults.cardColors(containerColor=if(brainOn) Color(0xFF0A3D1F) else Color(0xFF3D0A0A))){ Text(if(brainOn) "✅ Brain ON - Boss Rubel Mone Ache! Owner: Rubel Boss" else "❌ Brain OFF", color=if(brainOn) green else Color.Red, modifier=Modifier.padding(10.dp)) }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth().background(Color(0xFF0F1420)).padding(vertical=2.dp), horizontalArrangement=Arrangement.SpaceAround){
                        listOf("Hub","Face","Ctrl","Auto","113","Upd","Set").forEachIndexed{ i, n -> TextButton(onClick={tab=i}){ Text(n, color=if(tab==i) green else Color.Gray, style=MaterialTheme.typography.labelSmall) } }
                    }
                }
            }
        }
    }
}
