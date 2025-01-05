package com.example.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.models.Cart
import com.example.store.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.content.Context
import android.content.Intent
import android.net.Uri

class CartViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private var _cartItems = MutableStateFlow<List<Product>>(emptyList())
    var cartItems = _cartItems.asStateFlow()

    init {
        // Verifica e cria o carrinho se necessário
        auth.addAuthStateListener { authState ->
            val userId = authState.currentUser?.uid
            if (userId != null) {
                createCart(userId) // Criar carrinho se não existir
                loadCartItems() // Carregar itens do carrinho
            }
        }
    }

    private fun createCart(userId: String) {
        val cartRef = db.collection("carts").document(userId)

        viewModelScope.launch {
            val cartDocument = cartRef.get().await()

            if (!cartDocument.exists()) {

                cartRef.set(mapOf("userId" to userId, "itemsCount" to 0))
            }
        }
    }

    fun loadCartItems() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("carts")
            .document(userId)
            .collection("items")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    _cartItems.value = value.toObjects()
                }
            }
    }

    fun addToCart(product: Cart) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {
            val existingItem = cartRef
                .whereEqualTo("productId", product.productId)
                .get()
                .await()
                .documents
                .firstOrNull()

            if (existingItem != null) {
                val newQuantity = existingItem.getLong("quantity")?.toInt()?.plus(1) ?: 1
                cartRef.document(existingItem.id).update("quantity", newQuantity)
            } else {
                cartRef.add(product)
            }
        }
    }

    fun clearCart() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {
            // Remove todos os itens do carrinho
            cartRef.get().await().documents.forEach {
                cartRef.document(it.id).delete().await()
            }
        }
    }
}




