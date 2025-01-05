package com.example.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ProductViewModel : ViewModel() {

    private var _productList = MutableStateFlow<List<Product>>(emptyList())
    var productList = _productList.asStateFlow()

    init {
        getProductsList()
    }

    fun getProductsList() {
        var db = Firebase.firestore

        db.collection("products")
            .addSnapshotListener{ value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    _productList.value = value.toObjects()
                }
            }


    }
}
