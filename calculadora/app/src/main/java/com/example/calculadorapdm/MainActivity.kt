import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Importing necessary libraries
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Visor(text: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = text)
    }
}

@Composable
fun Calculadora(onClick: (String) -> Unit) {

    Column(modifier = Modifier.padding(30.dp)) {
        Row() {
            Button(onClick = { onClick("7") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "7")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("8") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "8")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("9") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "9")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("/") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "/")
            }
        }
        Row() {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("4") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "4")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("5") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "5")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("6") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "6")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick("*") }, modifier = Modifier.weight(0.25f)) {
                Text(text = "*")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    var text by remember { mutableStateOf("0") }

    Column() {
        Visor(text = text)
        Calculadora(onClick = { buttonText ->
            text = buttonText
        })
    }
}
