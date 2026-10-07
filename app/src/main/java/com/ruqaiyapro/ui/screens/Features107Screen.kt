package com.ruqaiyapro.ui.screens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ruqaiyapro.features.Features107Manager

@Composable
fun Features107Screen(){
    val ctx = LocalContext.current
    var search by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("All") }
    val cats = listOf("All","Core","Face","Control","Auto","Pro")
    var list by remember { mutableStateOf(Features107Manager.allFeatures) }
    LaunchedEffect(search,cat){
        var l = if(cat=="All") Features107Manager.allFeatures else Features107Manager.allFeatures.filter{ it.category==cat }
        if(search.isNotEmpty()) l = l.filter{ it.name.contains(search,true) || it.voice.contains(search,true) }
        list=l
    }
    Column(Modifier.fillMaxSize().padding(12.dp)){
        OutlinedTextField(value=search, onValueChange={search=it}, placeholder={Text("Search 107 features, Bangla...")}, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){ cats.take(3).forEach{ c-> FilterChip(selected=cat==c, onClick={cat=c}, label={Text(c)}) } }
        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){ cats.drop(3).forEach{ c-> FilterChip(selected=cat==c, onClick={cat=c}, label={Text(c)}) } }
        Spacer(Modifier.height(12.dp))
        LazyColumn{
            items(list){ f->
                var en by remember(f.id){ mutableStateOf(Features107Manager.isEnabled(ctx,f.id)) }
                Card(Modifier.fillMaxWidth().padding(vertical=4.dp), colors=CardDefaults.cardColors(containerColor=Color(0xFF111111))){
                    Column(Modifier.padding(12.dp)){
                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
                            Column(Modifier.weight(1f)){
                                Text("#${f.id} ${f.name}", color=Color.White)
                                Text(f.desc, color=Color.Gray, style=MaterialTheme.typography.bodySmall)
                                if(f.voice.isNotEmpty()) Text("\"${f.voice}\"", color=Color(0xFF00E676), style=MaterialTheme.typography.bodySmall)
                            }
                            Switch(checked=en, onCheckedChange={ en=it; Features107Manager.setEnabled(ctx,f.id,it); Toast.makeText(ctx,"${f.name} ${if(it)"ON" else "OFF"}",Toast.LENGTH_SHORT).show() })
                        }
                    }
                }
            }
        }
    }
}
