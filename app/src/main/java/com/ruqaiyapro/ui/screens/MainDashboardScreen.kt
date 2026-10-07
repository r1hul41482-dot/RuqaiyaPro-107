package com.ruqaiyapro.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun MainDashboardScreen(onStartService:()->Unit={}, onStopService:()->Unit={}){
    Column(Modifier.fillMaxSize().padding(16.dp)){
        Text("RuqaiyaPro Hub", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Button(onClick=onStartService, modifier=Modifier.fillMaxWidth()){ Text("Start Service - Hey Ruqaiya") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick=onStopService, modifier=Modifier.fillMaxWidth()){ Text("Stop Service") }
    }
}
