package com.ruqaiyapro.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(){
    var code by remember { mutableStateOf("# Python: add_feature(114, 'My Feature')") }
    var res by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(12.dp)){
        Text("RuqaiyaPro Self Updater - 113 GOD", style=MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value=code, onValueChange={code=it}, modifier=Modifier.fillMaxWidth().height(300.dp))
        Button(onClick={ res="Saved! Git push kore build hobe!" }, modifier=Modifier.fillMaxWidth()){ Text("🚀 Update & Build") }
        Text(res)
    }
}
