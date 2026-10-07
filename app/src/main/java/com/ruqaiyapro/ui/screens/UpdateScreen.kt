package com.ruqaiyapro.ui.screens
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ruqaiyapro.update.InAppUpdateManager

@Composable
fun UpdateScreen(){
    val ctx = LocalContext.current
    var code by remember { mutableStateOf("# Python example:\n# add_feature(108, 'Super Voice', 'New voice', 'Core')\n\n# Kotlin example:\n# Feature(108, \"My Feature\", \"Desc\", \"Core\")") }
    var lang by remember { mutableStateOf("python") }
    var result by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(12.dp)){
        Text("RuqaiyaPro In-App Updater - GOD MODE", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
            FilterChip(selected=lang=="python", onClick={lang="python"}, label={Text("Python")})
            FilterChip(selected=lang=="kotlin", onClick={lang="kotlin"}, label={Text("Kotlin")})
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value=code, onValueChange={code=it}, modifier=Modifier.fillMaxWidth().height(300.dp), placeholder={Text("Paste your feature code here...")})
        Spacer(Modifier.height(12.dp))
        Button(onClick={
            result = InAppUpdateManager.applyCodePatch(ctx, code, lang)
            // Copy push script to clipboard via Termux intent
            try{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/r1hul41482-dot/RuqaiyaPro-107/actions"))
                ctx.startActivity(intent)
            }catch(_:Exception){}
        }, modifier=Modifier.fillMaxWidth()){ Text("🚀 Update & Auto Build APK") }
        Spacer(Modifier.height(12.dp))
        Text(result)
        Spacer(Modifier.height(12.dp))
        Text("How it works:\n1. Paste Python/Kotlin\n2. Click Update\n3. Code -> /files/patches/\n4. Auto git push script generated\n5. GitHub builds APK in 3 min\n6. Download & auto-install", style=MaterialTheme.typography.bodySmall)
    }
}
