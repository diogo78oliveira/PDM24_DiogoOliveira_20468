package com.example.appnoticias20.core.domain

data class Article(
    val articleId: String,
    val title: String,
    val description: String,
    val content: String,
    val pubDate: String,
    val sourceName: String,
    val imageUrl: String
)