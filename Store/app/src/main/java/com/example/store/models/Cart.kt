package com.example.store.models

data class Cart(
    val productId: String = "",
    val name: String = "",
    val price: String = "",
    val quantity: Int = 1,
    val imageUrl: String = ""
)
