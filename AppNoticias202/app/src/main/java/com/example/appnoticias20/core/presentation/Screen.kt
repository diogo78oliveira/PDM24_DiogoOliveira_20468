package com.example.appnoticias20.core.presentation


sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object News : Screen

    @kotlinx.serialization.Serializable
    data class Article(val articleId: String) : Screen

}