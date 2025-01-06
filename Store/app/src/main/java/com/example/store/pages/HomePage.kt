package com.example.store.pages

import android.widget.Toast
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.store.models.Cart
import com.example.store.viewmodel.AuthViewModel
import com.example.store.viewmodel.CartViewModel
import com.example.store.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    viewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val products by viewModel.productList.collectAsStateWithLifecycle()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val exportedCode by cartViewModel.exportedCode.collectAsStateWithLifecycle()
    val importStatus by cartViewModel.importStatus.collectAsStateWithLifecycle()

    val showDialog = remember { mutableStateOf(false) }
    val showImportDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var importCode by remember { mutableStateOf("") }
    var exportMessage by remember { mutableStateOf("") }
    var showCopiedMessage by remember { mutableStateOf(false) }

    if (exportedCode != null) {
        exportMessage = "Carrinho exportado! Código: ${exportedCode}"
    }

    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    fun copyToClipboard() {
        val clip = android.content.ClipData.newPlainText("Código", exportedCode)
        clipboardManager.setPrimaryClip(clip)
        showCopiedMessage = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fortnite Store", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        authViewModel.signout()
                        navController.navigate("login")
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Sair", tint = Color.Red)
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge {
                                    Text("${cartItems.size}")
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { showDialog.value = true }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho", tint = Color.Gray)
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFE0F7FA),
                    titleContentColor = Color(0xFF00796B)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Image(
                                    painter = rememberImagePainter(product.imageUrl),
                                    contentDescription = "Imagem do produto",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(8.dp)
                                )
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = product.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF333333)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Preço: € ${product.price}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF388E3C)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = product.description,
                                        fontSize = 14.sp,
                                        color = Color(0xFF757575)
                                    )
                                }
                                Button(
                                    onClick = {
                                        val cartItem = Cart(
                                            productId = product.id,
                                            name = product.name,
                                            price = product.price,
                                            imageUrl = product.imageUrl,
                                            quantity = 1,
                                            totalPrice = product.price
                                        )
                                        cartViewModel.addToCart(cartItem)
                                        Toast.makeText(context, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Text(text = "Adicionar", color = Color.White)
                                }
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        cartViewModel.exportCart()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                ) {
                    Text(text = "Exportar Carrinho", color = Color.White)
                }

                TextField(
                    value = exportMessage,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                IconButton(
                    onClick = { copyToClipboard() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                ) {
                    Icon(Icons.Filled.AddCircle, contentDescription = "Copiar código")
                }

                if (showCopiedMessage) {
                    Text(
                        text = "Código copiado!",
                        color = Color.Green,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Button(
                    onClick = { showImportDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                ) {
                    Text(text = "Importar Carrinho", color = Color.White)
                }
            }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Carrinho", fontWeight = FontWeight.Bold) },
                    text = {
                        if (cartItems.isEmpty()) {
                            Text("O carrinho está vazio.")
                        } else {
                            LazyColumn {
                                items(cartItems) { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(item.imageUrl),
                                            contentDescription = "Imagem do produto",
                                            modifier = Modifier.size(50.dp)
                                        )
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = item.name, fontWeight = FontWeight.Bold)
                                            Text(text = "Preço unitário: € ${item.price}")
                                            Text(text = "Quantidade: ${item.quantity}")
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                navController.navigate("checkout")
                                showDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                        ) {
                            Text("Finalizar Compra", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD))
                        ) {
                            Text("Fechar", color = Color.White)
                        }
                    },
                    containerColor = Color(0xFFFFFFFF)
                )
            }

            if (showImportDialog.value) {
                AlertDialog(
                    onDismissRequest = { showImportDialog.value = false },
                    title = { Text("Importar Carrinho", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Insira o código do carrinho:")
                            TextField(
                                value = importCode,
                                onValueChange = { importCode = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (importStatus == false) {
                                Text(
                                    "Código inválido ou carrinho não encontrado.",
                                    color = Color.Red,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                cartViewModel.importCart(importCode)
                                showImportDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                        ) {
                            Text("Importar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showImportDialog.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD))
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                    },
                    containerColor = Color(0xFFFFFFFF)
                )
            }
        }
    }
}
