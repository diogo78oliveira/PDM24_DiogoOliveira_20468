    package com.example.store.models

    data class Cart(
        val productId: String = "",
        val name: String = "",
        val price: Double = 0.0,
        val quantity: Int = 0,
        val imageUrl: String = "",
        val totalPrice: Double = 0.0,
        val code: String = ""
    )
