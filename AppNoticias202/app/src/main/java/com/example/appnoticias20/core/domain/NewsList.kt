package com.example.appnoticias20.core.domain


data class NewsList(
    val nextPage: String?,
    val articles: List<Article>,
)