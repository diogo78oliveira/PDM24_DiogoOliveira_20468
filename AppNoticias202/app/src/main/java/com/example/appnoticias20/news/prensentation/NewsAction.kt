package com.example.appnoticias20.news.prensentation


sealed interface NewsAction {
    data object Paginate: NewsAction
}