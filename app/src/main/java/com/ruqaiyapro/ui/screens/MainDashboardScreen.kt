package com.ruqaiyapro.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainDashboardScreen(
    onStartService: () -> Unit = {},
    onStopService: () -> Unit = {}
){
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("RuqaiyaPro 107 - Boss Rubel", style = MaterialTheme.typography.headlineSmall)
        Text("Hey Ruqaiya Running...")
        Button(onClick = onStartService){ Text("Start Hey Ruqaiya Service") }
        OutlinedButton(onClick = onStopService){ Text("Stop Service") }
    }
}
