package com.example.newsapi

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.newsapi.api.NetworkResponse

@Composable
fun WeatherDetailsPage(city: String, viewModel: WeatherViewModel, navController: NavHostController) {
    val weatherResult = viewModel.weatherResult.observeAsState()


    LaunchedEffect(city) {
        viewModel.getData(city)
    }

    when (val result = weatherResult.value) {
        is NetworkResponse.Error -> {
            Text(text = "Erro: ${result.message}")
        }
        NetworkResponse.Loading -> {
            CircularProgressIndicator()
        }
        is NetworkResponse.Success -> {
            WeatherDetails(data = result.data)
        }
        null -> {

        }
    }
}
