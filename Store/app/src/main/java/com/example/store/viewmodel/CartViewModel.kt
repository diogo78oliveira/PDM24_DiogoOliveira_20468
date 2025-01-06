package com.example.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.models.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CartViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private var _cartItems = MutableStateFlow<List<Cart>>(emptyList())
    var cartItems = _cartItems.asStateFlow()
    private var _exportedCode = MutableStateFlow<String?>(null)
    val exportedCode = _exportedCode.asStateFlow()
    private var _importStatus = MutableStateFlow<Boolean?>(null)
    val importStatus = _importStatus.asStateFlow()



    init {
        auth.addAuthStateListener { authState ->
            val userId = authState.currentUser?.uid
            if (userId != null) {
                createCart(userId)
                loadCartItems()
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

    fun addToCart(cartItem: Cart) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {
            val existingItem = cartRef
                .whereEqualTo("productId", cartItem.productId)
                .get()
                .await()
                .documents
                .firstOrNull()

            if (existingItem != null) {
                val newQuantity = existingItem.getLong("quantity")?.toInt()?.plus(1) ?: 1
                val totalPrice = cartItem.price * newQuantity
                cartRef.document(existingItem.id)
                    .update("quantity", newQuantity, "totalPrice", totalPrice)
            } else {
                val totalPrice = cartItem.price * cartItem.quantity
                cartRef.add(cartItem.copy(totalPrice = totalPrice))
            }
        }
    }

    fun updateQuantity(item: Cart, newQuantity: Int) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {

            val totalPrice = item.price * newQuantity

            cartRef.document(item.productId)
                .update("quantity", newQuantity, "totalPrice", totalPrice)

            val updatedItem = item.copy(quantity = newQuantity, totalPrice = totalPrice)

            _cartItems.value = _cartItems.value.map {
                if (it.productId == item.productId) updatedItem else it
            }
        }
    }

    fun clearCart() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {
            cartRef.get().await().documents.forEach {
                cartRef.document(it.id).delete().await()
            }
        }
    }

    fun exportCart() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId)

        viewModelScope.launch {
            try {
                val cartCode = (100000..999999).random().toString()
                cartRef.update("code", cartCode).await()
                _exportedCode.value = cartCode
            } catch (e: Exception) {
                _exportedCode.value = null
            }
        }
    }

    fun importCart(cartCode: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("carts")
                    .whereEqualTo("code", cartCode)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val cartDocument = querySnapshot.documents.first()
                    val cartItems = cartDocument.reference
                        .collection("items")
                        .get()
                        .await()
                        .toObjects<Cart>()

                    _cartItems.value = cartItems
                    _importStatus.value = true
                } else {
                    _importStatus.value = false
                }
            } catch (e: Exception) {
                _importStatus.value = false
            }
        }
    }

    fun removeItem(item: Cart) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId).collection("items")

        viewModelScope.launch {
            try {
                val itemDocument = cartRef
                    .whereEqualTo("productId", item.productId)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()

                if (itemDocument != null) {
                    cartRef.document(itemDocument.id).delete().await()

                    _cartItems.value = _cartItems.value.filter { it.productId != item.productId }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

