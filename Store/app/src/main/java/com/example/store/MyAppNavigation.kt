package com.example.store

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.store.pages.CheckoutPage
import com.example.store.pages.HomePage
import com.example.store.pages.LoginPage
import com.example.store.pages.SignupPage
import com.example.store.viewmodel.AuthViewModel
import com.example.store.viewmodel.CartViewModel
import com.example.store.viewmodel.ProductViewModel


@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val productViewModel = ProductViewModel()
    val cartViewModel = CartViewModel()


    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage(modifier,navController,authViewModel)
        }
        composable("Registo"){
            SignupPage(modifier,navController,authViewModel)
        }
        composable("home"){
            HomePage(productViewModel,cartViewModel,authViewModel,navController)
        }
        composable("checkout"){
            CheckoutPage(cartViewModel,navController)
        }

    })
}