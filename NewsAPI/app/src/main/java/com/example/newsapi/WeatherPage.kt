package com.example.newsapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.newsapi.api.NetworkResponse
import com.example.newsapi.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel, navController: NavController) {

    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Pesquisar cidade") }
            )
            IconButton(onClick = {
                navController.navigate("weather_details/$city")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar cidade"
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CityRow(cityName = "London", onClick = {
                navController.navigate("weather_details/London")
            })
            CityRow(cityName = "Paris", onClick = {
                navController.navigate("weather_details/Paris")
            })
            CityRow(cityName = "Braga", onClick = {
                navController.navigate("weather_details/Braga")
            })
            CityRow(cityName = "California", onClick = {
                navController.navigate("weather_details/California")
            })
            CityRow(cityName = "Hong Kong", onClick = {
                navController.navigate("weather_details/Hong Kong")
            })
        }
    }
}


@Composable
fun CityRow(cityName: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = cityName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp)
        )
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Pesquisar $cityName"
            )
        }
    }
}



@Composable
fun WeatherDetails(data : WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Icon Localizacao",
                modifier = Modifier.size(42.dp)
            )
            Text(text = data.location.name, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(9.dp))
            Text(text = data.location.country, fontSize = 20.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "${data.current.temp_c} ° c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition Icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(18.dp))
        Card{
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    HeatherKeyVal("Humidade", data.current.humidity)
                    HeatherKeyVal("Vento", data.current.wind_kph+" km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    HeatherKeyVal("Direçao do Vento", data.current.wind_dir)
                    HeatherKeyVal("Percipitaçao", data.current.precip_mm+" km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    HeatherKeyVal("Hora Local", data.location.localtime.split(" ")[1])
                    HeatherKeyVal("Data Local", data.location.localtime.split(" ")[0])
                }
            }

        }
    }
}


@Composable
fun HeatherKeyVal(key : String, value : String) {
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}


