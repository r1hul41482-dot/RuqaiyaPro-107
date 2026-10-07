package com.ruqaiyapro.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ruqaiyapro.features.Features107Manager
@Composable
fun Features107Screen(){
    val ctx = LocalContext.current
    var search by remember { mutableStateOf("") }
    val list = remember(search){
        if(search.isBlank()) Features107Manager.allFeatures else Features107Manager.allFeatures.filter{ it.name.contains(search, true) }
    }
    Column(Modifier.fillMaxSize().padding(12.dp)){
        OutlinedTextField(value=search, onValueChange={search=it}, modifier=Modifier.fillMaxWidth(), placeholder={Text("Search 107...")})
        Spacer(Modifier.height(8.dp))
        LazyColumn{
            items(list){ f->
                var en by remember(f.id){ mutableStateOf(Features107Manager.isEnabled(ctx,f.id)) }
                Card(Modifier.fillMaxWidth().padding(vertical=4.dp)){
                    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement=Arrangement.SpaceBetween){
                        Column(Modifier.weight(1f)){ Text("#${f.id} ${f.name}"); Text(f.desc, style=MaterialTheme.typography.bodySmall) }
                        Switch(checked=en, onCheckedChange={ en=it; Features107Manager.setEnabled(ctx,f.id,it) })
                    }
                }
            }
        }
    }
}
