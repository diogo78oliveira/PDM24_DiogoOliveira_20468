package com.example.newsapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.api.Constant
import com.example.newsapi.api.NetworkResponse
import com.example.newsapi.api.RetrofitInstance
import com.example.newsapi.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

   fun getData(city : String){

        _weatherResult.value = NetworkResponse.Loading

       viewModelScope.launch {
           try {
               val response = weatherApi.getWeather(Constant.apiKey,city)
               if(response.isSuccessful){
                   response.body()?.let {
                       _weatherResult.value = NetworkResponse.Success(it)
                   }
               }else{
                   _weatherResult.value = NetworkResponse.Error("Erro")
               }
           }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Erro")
            }
       }
   }


}