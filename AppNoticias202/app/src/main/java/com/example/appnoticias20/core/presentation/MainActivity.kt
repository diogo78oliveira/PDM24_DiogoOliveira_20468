package com.example.appnoticias20.core.prensentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.appnoticias20.core.data.NewsRepositoryImpl
import com.example.appnoticias20.core.news.prensentation.NewsScreenCore
import com.example.appnoticias20.core.prensentation.ui.theme.NewsAppTheme
import com.example.appnoticias20.core.presentation.Screen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsAppTheme {
                Navigation()
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.News
        ) {

            composable<Screen.News> {
                NewsScreenCore {
                    navController.navigate(Screen.Article(it))
                }
            }


            composable<Screen.Article> { backStackEntry ->
                val article: Screen.Article = backStackEntry.toRoute()
                article.articleId
            }

        }

    }

}
