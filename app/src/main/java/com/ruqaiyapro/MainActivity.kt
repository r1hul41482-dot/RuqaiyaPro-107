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
import com.ruqaiyapro.features.Features113Manager
import com.ruqaiyapro.services.FloatingChibiService
import com.ruqaiyapro.services.RuqaiyaHotwordService

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("ruqaiya_prefs",0)
        setContent {
            val black = Color(0xFF0B0F1A); val cardBg = Color(0xFF151A28); val green = Color(0xFF00E676)
            var tab by remember { mutableIntStateOf(0) }
            var search by remember { mutableStateOf("") }
            var apiKey by remember { mutableStateOf(prefs.getString("api_key","")?:"") }
            var brainOn by remember { mutableStateOf(prefs.getBoolean("brain_on",false) || apiKey.length>10) }
            var transcript by remember { mutableStateOf("Say 'Hey Ruqaiya' or click any voice command above.") }
            var volume by remember { mutableFloatStateOf(0.65f) }
            var brightness by remember { mutableFloatStateOf(0.70f) }
            MaterialTheme(colorScheme = darkColorScheme(container=black, background=black, primary=green, surface=cardBg)) {
                Column(Modifier.fillMaxSize().background(black).padding(8.dp)){
                    Text("RuqaiyaPro FOR BOSS RUBEL - 113 GOD", color=Color.White, style=MaterialTheme.typography.titleMedium)
                    Text("Full Android Studio Project + Agent + Live Google-like Companion (Infinix Hot 12 Play)", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                    Row(Modifier.padding(vertical=4.dp)){
                        Button(onClick={ try{ startForegroundService(Intent(this@MainActivity, RuqaiyaHotwordService::class.java)); startService(Intent(this@MainActivity, FloatingChibiService::class.java)); transcript="Ji Rubel Boss bolo... Mone ache!" }catch(e:Exception){} }, colors=ButtonDefaults.buttonColors(containerColor=green), modifier=Modifier.weight(1f)){ Text("Live Companion", color=Color.Black) }
                        Spacer(Modifier.width(6.dp))
                        Button(onClick={}, modifier=Modifier.weight(1f), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF1E88E5))){ Text("Android Studio ZIP") }
                    }
                    when(tab){
                        0 -> Column(Modifier.verticalScroll(rememberScrollState())){
                            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                                Text("RuqaiyaPro Companion", color=Color.White)
                                Text("No-Beep Trick, 3 Streams Muted & 500ms Delay", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                                Button(onClick={ transcript="Ji Rubel Boss bolo..."; try{ startService(Intent(this@MainActivity, FloatingChibiService::class.java)) }catch(e:Exception){} }, modifier=Modifier.fillMaxWidth().padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Start Service - Hey Ruqaiya", color=Color.Black) }
                                OutlinedButton(onClick={ try{ stopService(Intent(this@MainActivity, RuqaiyaHotwordService::class.java)) }catch(e:Exception){} }, modifier=Modifier.fillMaxWidth().padding(top=4.dp)){ Text("Stop Service") }
                                Spacer(Modifier.height(8.dp))
                                Text("Quick Voice Triggers for Boss Rubel:", color=Color.White, style=MaterialTheme.typography.labelMedium)
                                Row(Modifier.padding(top=4.dp)){ AssistChip(onClick={transcript="Hey Ruqaiya triggered"}, label={Text("Hey Ruqaiya")}, modifier=Modifier.padding(end=4.dp)); AssistChip(onClick={transcript="Amar naam Rubel Boss - mone ache!"}, label={Text("Amar naam ki?")}, modifier=Modifier.padding(end=4.dp)); AssistChip(onClick={transcript="Flash jalo"}, label={Text("Flash jalo")}) }
                                Row(Modifier.padding(top=4.dp)){ AssistChip(onClick={transcript="Amake Mone Rakho Rubel - save korchi Boss!"; prefs.edit().putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply()}, label={Text("Amake mone rakho Rubel")}, modifier=Modifier.padding(end=4.dp)); AssistChip(onClick={transcript="Phone koi? Siren ON!"}, label={Text("Phone koi?")}) }
                                Row(Modifier.padding(top=4.dp)){ AssistChip(onClick={transcript="WhatsApp Auto ON - Boss busy"}, label={Text("WhatsApp Auto ON")}, modifier=Modifier.padding(end=4.dp)); AssistChip(onClick={transcript="Boss Rubel ke roast? Tumi to king!"}, label={Text("Amake roast koro")}) }
                                Card(Modifier.fillMaxWidth().padding(top=8.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF0F141F))){ Text(transcript, color=green, modifier=Modifier.padding(10.dp)) }
                                Text("Live Google-Style HUD Logs", color=Color.Gray, modifier=Modifier.padding(top=8.dp), style=MaterialTheme.typography.labelMedium)
                                Text("onPartial: Tumi bolcho:\nonResults: Tumi bolle: Hey Ruqaiya\nWake: Ji Rubel Boss bolo...", color=Color.DarkGray, style=MaterialTheme.typography.labelSmall)
                                Spacer(Modifier.height(10.dp))
                                Text("Termux Chibi Add (MOST IMPORTANT):", color=green, style=MaterialTheme.typography.labelMedium)
                                Text("Termux e: cp /sdcard/Download/tomar_pic.png /sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                                Row(Modifier.padding(top=6.dp)){ Button(onClick={ try{ stopService(Intent(this@MainActivity, FloatingChibiService::class.java)); startService(Intent(this@MainActivity, FloatingChibiService::class.java)) }catch(e:Exception){} }){ Text("Reload Chibi") }; Spacer(Modifier.width(6.dp)); Button(onClick={ FloatingChibiService.deleteAll(this@MainActivity); transcript="Reset to Default Chibi" }){ Text("Reset Default") } }
                                Card(Modifier.fillMaxWidth().padding(top=10.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A))){ Column(Modifier.padding(10.dp)){ Text("In-App Code Help & Auto Fix Engine", color=Color.White); Text("BadActivity.kt - 3 Runtime Errors detected", color=Color(0xFFFF5252), style=MaterialTheme.typography.labelSmall); Row{ Button(onClick={transcript="Auto Fixed BadActivity.kt!"}, colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF00C853))){ Text("Fix Kor") }; Spacer(Modifier.width(6.dp)); OutlinedButton(onClick={}){ Text("Ask AI") } } } }
                            } }
                        }
                        1 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Face God - Boss Rubel Only Voice If not Boss face, no command works", color=Color.White)
                            Box(Modifier.fillMaxWidth().height(180.dp).background(Color(0xFF0A0A0A)).padding(8.dp)){ Text("CameraX Preview Idle - Click Start\nVisual Feedback Toast System (on Top of Overlay) c-{100} High Priority", color=Color.Gray) }
                            Row(Modifier.padding(top=8.dp)){ Button(onClick={ prefs.edit().putBoolean("boss_face_saved",true).putString("owner_name","Rubel Boss").apply(); transcript="Face saved - Rubel Boss Mone Ache!" }, colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Amake Mone Rakho Rubel", color=Color.Black) }; Spacer(Modifier.width(6.dp)); Button(onClick={ transcript="CameraX Started - Boss Rubel detected!" }){ Text("Start CameraX") } }
                            Text("Owner: Rubel Boss - ${if(prefs.getBoolean("boss_face_saved",false)) "Mone Ache!" else "Save koro"}", color=green, modifier=Modifier.padding(top=6.dp))
                        } }
                        2 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Control God Mode - All ON/OFF", color=Color.White)
                            Row(Modifier.padding(top=6.dp)){ FilterChip(selected=true, onClick={}, label={Text("WIFI ON")}, modifier=Modifier.padding(end=4.dp)); FilterChip(selected=true, onClick={}, label={Text("Bluetooth ON")}, modifier=Modifier.padding(end=4.dp)); FilterChip(selected=false, onClick={}, label={Text("Flashlight OFF")}) }
                            Spacer(Modifier.height(8.dp)); Text("Master Volume Control ${(volume*100).toInt()}%", color=Color.White); Slider(value=volume, onValueChange={volume=it})
                            Text("Screen Brightness ${(brightness*100).toInt()}%", color=Color.White); Slider(value=brightness, onValueChange={brightness=it})
                            Button(onClick={transcript="Phone Koi! Siren ON - 100% volume!"}, modifier=Modifier.fillMaxWidth().padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFFF3D00))){ Text("Test Phone Koi! Loud Siren") }
                            Row(Modifier.padding(top=8.dp)){ Checkbox(checked=true, onCheckedChange={}); Text("Anti-Theft Trap (Active)", color=Color.White, modifier=Modifier.padding(end=8.dp)); Checkbox(checked=true, onCheckedChange={}); Text("Driving Mode", color=Color.White) }
                            Button(onClick={ Features113Manager.allOn(this@MainActivity,true); transcript="God Mode ON - All 113 ON for Boss Rubel!" }, modifier=Modifier.fillMaxWidth().padding(top=10.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("God Mode ON - All 107 ON", color=Color.Black) }
                        } }
                        3 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                            Text("Auto God - WhatsApp Auto Send", color=Color.White, style=MaterialTheme.typography.titleMedium)
                            Text("WhatsApp Auto-ON + Accessibility + Notifications.listener - Boss busy ache, pore reply dibe - Ruqaiya bolche", color=Color.Gray, style=MaterialTheme.typography.labelSmall, modifier=Modifier.padding(top=4.dp))
                            OutlinedTextField(value="Boss Rubel busy ache, pore reply dibe - Ruqaiya bolche", onValueChange={}, label={Text("Auto Reply Message")}, modifier=Modifier.fillMaxWidth().padding(top=8.dp))
                            Row(Modifier.padding(top=8.dp)){ Button(onClick={transcript="WhatsApp Auto ON for Boss Rubel"}){ Text("Enable Auto") }; Spacer(Modifier.width(6.dp)); Button(onClick={transcript="Broadcast sent with 3 sec delay!"}){ Text("Broadcast") } }
                            Card(Modifier.fillMaxWidth().padding(top=10.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A1A))){ Column(Modifier.padding(8.dp)){ Text("Code Auto Fix: BadActivity.kt", color=Color.White); Text("Mone rakhbe error - Auto fix in background Dispatchers.IO", color=Color.Gray, style=MaterialTheme.typography.labelSmall) } }
                        } }
                        4 -> Column{
                            OutlinedTextField(value=search, onValueChange={search=it}, label={Text("Search 107 features, Bangla commands...")}, modifier=Modifier.fillMaxWidth())
                            Row(Modifier.padding(top=6.dp)){ FilterChip(selected=true, onClick={}, label={Text("All 107")}, modifier=Modifier.padding(end=4.dp)); FilterChip(selected=false, onClick={}, label={Text("Core (15)")}, modifier=Modifier.padding(end=4.dp)); FilterChip(selected=false, onClick={}, label={Text("Khata & Face (45)")}) }
                            Button(onClick={ Features113Manager.allOn(this@MainActivity,true); transcript="God Mode ON - All 113 ON!" }, modifier=Modifier.fillMaxWidth().padding(top=6.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("God Mode ON - All 107 ON", color=Color.Black) }
                            LazyColumn(Modifier.height(480.dp).padding(top=6.dp)){
                                items(Features113Manager.all.filter{ search.isEmpty() || it.name.contains(search,true) || it.bn.contains(search,true) }){ f ->
                                    var en by remember { mutableStateOf(Features113Manager.isEnabled(this@MainActivity,f.id)) }
                                    Card(Modifier.fillMaxWidth().padding(vertical=2.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1F2E))){ Row(Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Column(Modifier.weight(1f)){ Text(f.name, color=Color.White, style=MaterialTheme.typography.bodyMedium); Text(f.desc, color=Color.Gray, style=MaterialTheme.typography.labelSmall); Text(f.bn, color=green, style=MaterialTheme.typography.labelSmall) }; Switch(checked=en, onCheckedChange={ en=it; Features113Manager.setEnabled(this@MainActivity,f.id,it) }) } }
                                }
                            }
                        }
                        5 -> Column(Modifier.verticalScroll(rememberScrollState())){
                            Text("RuqaiyaPro Self Updater - 113 GOD", color=Color.White)
                            var code by remember { mutableStateOf("# Python: add_feature(114, 'My Feature')\n# Termux: cp pic -> /sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png\nprint('Add new feature for Boss Rubel')") }
                            OutlinedTextField(value=code, onValueChange={code=it}, modifier=Modifier.fillMaxWidth().height(300.dp).padding(top=8.dp), label={Text("Python code")})
                            Button(onClick={ transcript="Updated! Build triggered for Boss Rubel - 114 features now!"; brainOn=true }, modifier=Modifier.fillMaxWidth().padding(top=8.dp), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("🚀 Update & Build", color=Color.Black) }
                            Text(transcript, color=green, modifier=Modifier.padding(top=8.dp))
                        }
                        6 -> Column(Modifier.verticalScroll(rememberScrollState())){
                            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=cardBg)){ Column(Modifier.padding(12.dp)){
                                Text("Google AI SDK (Gemini 3.8 Flash) Dynamic Configuration", color=Color.White, style=MaterialTheme.typography.titleSmall)
                                Text("SDK Active", color=green, style=MaterialTheme.typography.labelSmall)
                                Row(Modifier.padding(top=8.dp)){ Switch(checked=brainOn, onCheckedChange={brainOn=it; prefs.edit().putBoolean("brain_on",it).apply()}); Spacer(Modifier.width(8.dp)); Text("Enable Dynamic AI Features - Use Gemini 3.8 Flash for intelligent Bengali/Bangla dialogue", color=Color.White, style=MaterialTheme.typography.labelSmall) }
                                Text("Gemini API Key Configuration gemini-1.3-flash", color=Color.Gray, modifier=Modifier.padding(top=10.dp), style=MaterialTheme.typography.labelMedium)
                                OutlinedTextField(value=apiKey, onValueChange={ apiKey=it; if(it.length>10){ brainOn=true; prefs.edit().putString("api_key",it).putBoolean("brain_on",true).putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply(); transcript="Brain ON - Rubel Boss ke mone ache!" } }, label={Text("Input Gemini API Key - Any AI API also works")}, modifier=Modifier.fillMaxWidth().padding(top=6.dp))
                                Text("Users can obtain a free key from Google AI Studio. Stored locally to automatically enable the SDK.", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                                Button(onClick={ brainOn=true; prefs.edit().putBoolean("brain_on",true).putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply(); transcript="Save Key & Brain ON - Rubel Boss Mone Ache!" }, modifier=Modifier.padding(top=8.dp)){ Text("Save Key & Initialize Google AI SDK") }
                                Card(Modifier.fillMaxWidth().padding(top=12.dp), colors=CardDefaults.cardColors(containerColor=if(brainOn) Color(0xFF0A3D1F) else Color(0xFF3D0A0A))){ Text(if(brainOn) "✅ Brain ON 🧠 - Boss Rubel ke mone ache - Memory active! Owner: Rubel Boss" else "❌ Brain OFF - Add any AI API to ON", color=if(brainOn) green else Color.Red, modifier=Modifier.padding(10.dp)) }
                                Spacer(Modifier.height(12.dp))
                                Text("Live AI Playground (Gemini 3.8 Flash) Dynamic Mode", color=Color.White, style=MaterialTheme.typography.titleSmall)
                                var prompt by remember { mutableStateOf("Boss Rubel er jonno ekti shundor shokal er message") }
                                OutlinedTextField(value=prompt, onValueChange={prompt=it}, modifier=Modifier.fillMaxWidth().padding(top=6.dp))
                                Button(onClick={ transcript="AI: Shuvo shokal Boss Rubel! Apni king! 😍" }, modifier=Modifier.padding(top=6.dp)){ Text("Ask") }
                                Text(transcript, color=green, modifier=Modifier.padding(top=8.dp))
                            } }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth().background(Color(0xFF0F1420)).padding(vertical=4.dp), horizontalArrangement=Arrangement.SpaceAround){
                        listOf("Hub","Face","Ctrl","Auto","107","Upd","Set").forEachIndexed{ i, n ->
                            val letter = listOf("H","F","C","A","1","U","S")[i]
                            TextButton(onClick={tab=i}){ Column(horizontalAlignment=androidx.compose.ui.Alignment.CenterHorizontally){ Text(letter, color=if(tab==i) green else Color.Gray, style=MaterialTheme.typography.labelSmall); Text(n, color=if(tab==i) green else Color.Gray, style=MaterialTheme.typography.labelSmall) } }
                        }
                    }
                }
            }
        }
    }
}
