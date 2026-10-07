package com.ruqaiyapro
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import com.ruqaiyapro.features.Features113Manager
import com.ruqaiyapro.services.FloatingChibiService
import com.ruqaiyapro.services.RuqaiyaHotwordService
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            val prefs = getSharedPreferences("ruqaiya_prefs",0)
            setContent {
                val black = Color(0xFF000000); val green = Color(0xFF00E676)
                var tab by remember { mutableStateOf(0) }
                var search by remember { mutableStateOf("") }
                var apiKey by remember { mutableStateOf(prefs.getString("api_key","")?:"") }
                var brainOn by remember { mutableStateOf(prefs.getBoolean("brain_on",false) || apiKey.length>10) }
                var transcript by remember { mutableStateOf("Boss Rubel - Bolo Hey Ruqaiya") }
                MaterialTheme(colorScheme = darkColorScheme(container=black, background=black, primary=green)){
                    Column(Modifier.fillMaxSize().background(black).padding(6.dp)){
                        Text("RuqaiyaPro FOR BOSS RUBEL - 113 GOD + MEMORY", color=green)
                        Text(if(brainOn) "Brain ON 🧠 - Boss Rubel ke mone ache!" else "Brain OFF - API dao", color=if(brainOn) green else Color.Red, style=MaterialTheme.typography.labelSmall)
                        Row{ Button(onClick={ try{ startForegroundService(Intent(this@MainActivity, RuqaiyaHotwordService::class.java)); startService(Intent(this@MainActivity, FloatingChibiService::class.java)); transcript="Service Started - Ji Rubel Boss bolo..." }catch(e:Exception){} }, colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Live Companion") } }
                        when(tab){
                            0 -> Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=Color(0xFF111111))){ Column(Modifier.padding(10.dp)){
                                Text("Hub - Boss Rubel", color=Color.White)
                                Text("Amake Mone Rakho Rubel - save korchi", color=green)
                                Button(onClick={ transcript="Ji Rubel Boss bolo... Mone ache Boss Rubel!"; prefs.edit().putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply() }, modifier=Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("Amake Mone Rakho Rubel - Boss") }
                                Text("Termux: cp /sdcard/Download/tomar_pic.png /sdcard/Android/data/com.ruqaiyapro/files/chibi_custom.png", color=Color.Gray, style=MaterialTheme.typography.labelSmall)
                                Row{ Button(onClick={ try{ stopService(Intent(this@MainActivity, FloatingChibiService::class.java)); startService(Intent(this@MainActivity, FloatingChibiService::class.java)) }catch(e:Exception){} }){ Text("Reload Chibi") }; Spacer(Modifier.width(6.dp)); Button(onClick={ FloatingChibiService.deleteAll(this@MainActivity) }){ Text("Reset Default") } }
                                Text(transcript, color=green, modifier=Modifier.padding(8.dp))
                            } }
                            4 -> Column{
                                OutlinedTextField(value=search, onValueChange={search=it}, label={Text("Search 113 features...")}, modifier=Modifier.fillMaxWidth())
                                Button(onClick={ Features113Manager.allOn(this@MainActivity,true) }, modifier=Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor=green)){ Text("God Mode ON - All 113 ON - Boss Rubel") }
                                Button(onClick={ Features113Manager.allOn(this@MainActivity,false) }, modifier=Modifier.fillMaxWidth()){ Text("All OFF") }
                                LazyColumn(Modifier.height(500.dp)){
                                    items(Features113Manager.all.filter{ it.name.contains(search,true) || it.bn.contains(search,true) }){ f ->
                                        var en by remember { mutableStateOf(Features113Manager.isEnabled(this@MainActivity,f.id)) }
                                        Card(Modifier.fillMaxWidth().padding(2.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF1A1A1A))){ Row(Modifier.padding(8.dp), horizontalArrangement=Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth()){ Column{ Text(f.name, color=Color.White); Text(f.bn, color=green, style=MaterialTheme.typography.labelSmall) }; Switch(checked=en, onCheckedChange={ en=it; Features113Manager.setEnabled(this@MainActivity,f.id,it) }) } }
                                    }
                                }
                            }
                            6 -> Column{
                                Text("Settings - Any AI API = Brain ON", color=Color.White)
                                OutlinedTextField(value=apiKey, onValueChange={ apiKey=it; if(it.length>10){ brainOn=true; prefs.edit().putString("api_key",it).putBoolean("brain_on",true).putString("owner_name","Rubel Boss").apply() } }, label={Text("Gemini/OpenAI/Claude Any API Key")}, modifier=Modifier.fillMaxWidth())
                                Button(onClick={ brainOn=true; prefs.edit().putBoolean("brain_on",true).putString("owner_name","Rubel Boss").putBoolean("boss_remembered",true).apply() }){ Text("Save Key & Brain ON - Mone Rakhbe Rubel Boss") }
                                Text(if(brainOn) "✅ Brain ON - Rubel Boss ke mone ache - Memory active - Amake mone rakho bollei mone rakhbe!" else "❌ Brain OFF", color=if(brainOn) green else Color.Red)
                                Spacer(Modifier.height(10.dp))
                                Text("Owner: ${prefs.getString("owner_name","Rubel Boss")} - ${if(prefs.getBoolean("boss_remembered",false)) "Mone Ache!" else "Mone rakho"}", color=Color.White)
                            }
                            else -> Box(Modifier.fillMaxSize()){ Text("Tab $tab - ${listOf("Hub","Face","Ctrl","Auto","107-113","Upd","Set")[tab]} - Boss Rubel er jonno", color=Color.White, modifier=Modifier.padding(20.dp)) }
                        }
                        Spacer(Modifier.weight(1f))
                        Row(Modifier.fillMaxWidth().background(Color(0xFF111111)), horizontalArrangement=Arrangement.SpaceAround){
                            listOf("Hub","Face","Ctrl","Auto","113","Upd","Set").forEachIndexed{ i, n -> TextButton(onClick={tab=i}){ Text(n, color=if(tab==i) green else Color.Gray) } }
                        }
                    }
                }
            }
        }catch(e:Exception){ try{ setContent{ Text("Crash Safe: ${e.message}") } }catch(ex:Exception){} }
    }
}
