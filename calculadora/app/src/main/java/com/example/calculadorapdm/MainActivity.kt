package com.example.calculadorapdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadorapdm.ui.theme.CalculadoraPDMTheme
import com.example.calculadora.calculatorLogic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraPDMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    CalculadoraView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculadoraView(modifier: Modifier = Modifier) {
    var displayValue by remember { mutableStateOf("0") }
    val calculatorLogic = remember { calculatorLogic() }

    Column(modifier = modifier.fillMaxSize()
    ) {
        Visor(text = displayValue)
        Spacer(modifier = Modifier.height(16.dp))
        Calculadora(onClick = { buttonText ->
            val result = calculatorLogic.onInput(buttonText)
            displayValue = try {

                val doubleValue = result.toDouble()

                if (doubleValue % 1 == 0.0) {
                    doubleValue.toInt().toString()
                } else {
                    result
                }
            } catch (e: NumberFormatException) {
                result
            }
        })
    }
}

@Composable
fun Visor(text: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = text, style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun CalculadoraButton(value: String, onClick: (String) -> Unit, modifier: Modifier = Modifier, isOperation: Boolean = false) {
    val backgroundColor = if (isOperation) Color(0xFF3b3a30) else Color(0xFF484f4f)

    Button(
        onClick = { onClick(value) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(text = value, color = Color.White)
    }
}

@Composable
fun Calculadora(onClick: (String) -> Unit) {
    val buttonValues = listOf(
        listOf("7", "8", "9", "÷"),
        listOf("4", "5", "6", "×"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "=", "+"),
        listOf("CE")
    )

    Column(modifier = Modifier.padding(30.dp)) {
        buttonValues.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { value ->
                    val isOperation = value in listOf("÷", "×", "-", "+", "=", "CE")
                    CalculadoraButton(
                        value = value,
                        onClick = onClick,
                        modifier = Modifier.weight(1f),
                        isOperation = isOperation
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    CalculadoraView()
}
