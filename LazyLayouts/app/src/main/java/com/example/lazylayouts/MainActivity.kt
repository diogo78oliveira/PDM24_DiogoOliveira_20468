package com.example.lazylayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormScreen()
        }
    }
}

data class FormLine(
    var name: String = "",
    var type: String = "",
    var hint: String? = null
)

@Composable
fun FormScreen() {
    val formLines = remember { mutableStateListOf<FormLine>(
        FormLine(),
        FormLine(),
        FormLine()
    ) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(formLines) { formLine ->
                FormLineInput(formLine)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FormLineInput(formLine: FormLine) {
    var name by remember { mutableStateOf(formLine.name) }
    var type by remember { mutableStateOf(formLine.type) }
    var hint by remember { mutableStateOf(formLine.hint) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = name,
            onValueChange = {
                name = it
                formLine.name = it
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = type,
            onValueChange = {
                type = it
                formLine.type = it
            },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = hint ?: "",
            onValueChange = {
                hint = if (it.isEmpty()) null else it
                formLine.hint = hint
            },
            label = { Text("Hint (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
