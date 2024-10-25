package com.example.constraintlayout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.constraintlayout.ui.theme.ConstraintLayoutTheme
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConstraintLayoutTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavegacaoAPP()
                }
            }
        }
    }
}

@Composable
fun NavegacaoAPP() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            EcraInicio(navController)
        }
        composable("destino") {
            EcraDestino()
        }
    }
}

@Composable
fun EcraInicio(navController: NavController) {
    Button(onClick = { navController.navigate("destino") }) {
        Text("Ir para o destino")
    }
}

@Composable
fun EcraDestino() {
    Text("DENIS")
}


