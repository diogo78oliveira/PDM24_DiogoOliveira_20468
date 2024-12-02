package com.example.newsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapi.ui.theme.NewsAPITheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        setContent {
            NewsAPITheme {
                val navController = rememberNavController()

                Column(modifier = Modifier.fillMaxSize()) {
                    HomeButton(navController)

                    NavHost(navController = navController, startDestination = "weather_page") {
                        composable("weather_page") {
                            WeatherPage(weatherViewModel, navController)
                        }
                        composable("weather_details/{city}") { backStackEntry ->
                            val city = backStackEntry.arguments?.getString("city") ?: ""
                            WeatherDetailsPage(city, weatherViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeButton(navController: NavController) {
    IconButton(
        onClick = {
            navController.navigate("weather_page") {
                popUpTo("weather_page") { inclusive = true }
            }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Voltar para a página inicial"
        )
    }
}
