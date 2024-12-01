package com.example.appnoticias20.news.prensentation

import com.example.appnoticias20.core.domain.Article


data class NewsState(
    val articleList: List<Article> = emptyList(),
    val nextPage: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
