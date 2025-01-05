package com.example.store.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.store.viewmodel.ProductViewModel
import com.example.store.viewmodel.CartViewModel

// Função para gerar o link do carrinho
fun generateCartLink(userId: String): String {
    // Aqui você pode gerar um link único para o carrinho, por exemplo:
    return "https://lojadefortnite/cart/$userId"
}

// Função para compartilhar o link
fun shareCartLink(context: Context, cartLink: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, cartLink)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar carrinho"))
}

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

    // Estado do dialog para controlar quando mostrar o carrinho
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Definindo o userId manualmente para testes
    val userId = "12345"  // Aqui você pode inserir o userId manualmente

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Store", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Botão "Sair" no canto superior esquerdo
                    IconButton(onClick = {
                        authViewModel.signout()
                        navController.navigate("login") // Substitua "login" pela sua tela de login
                    }) {
                        Text("Sair", color = Color.Red) // Texto "Sair" em vermelho
                    }
                },
                actions = {
                    // Botão de carrinho
                    IconButton(onClick = {
                        showDialog.value = true // Exibir o pop-up (Dialog)
                    }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho")
                    }
                }
            )
        }
    ) { paddingValues ->

        // Conteúdo da página com os produtos
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products) { product ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Imagem do produto
                        Image(
                            painter = rememberImagePainter(product.imageUrl),
                            contentDescription = "Imagem do produto",
                            modifier = Modifier.size(100.dp)
                        )

                        // Informações do produto
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = product.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Preço: ${product.price}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )

                            Text(
                                text = product.description,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }

                        // Botão "Adicionar ao Carrinho"
                        Button(
                            onClick = {
                                val cartItem = Cart(
                                    productId = product.id,
                                    name = product.name,
                                    price = product.price,
                                    imageUrl = product.imageUrl
                                )
                                cartViewModel.addToCart(cartItem)
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "Adicionar")
                        }
                    }
                }
            }

            // Exibe o Dialog (pop-up) com o conteúdo do carrinho
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false }, // Fechar o pop-up
                    title = { Text("Carrinho") },
                    text = {
                        if (cartItems.isEmpty()) {
                            Text("Carrinho vazio")
                        } else {
                            LazyColumn {
                                items(cartItems) { item ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(item.imageUrl),
                                            contentDescription = "Imagem do produto",
                                            modifier = Modifier.size(50.dp)
                                        )
                                        Text(text = item.name)
                                        Text(text = "Preço: ${item.price}")
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Navegar para a página de checkout
                                navController.navigate("checkout")
                                showDialog.value = false // Fechar o pop-up
                            }
                        ) {
                            Text("Finalizar Compra")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDialog.value = false
                        }) {
                            Text("Fechar")
                        }
                    }
                )
            }

            // Alinhando o FloatingActionButton no canto inferior direito
            FloatingActionButton(
                onClick = {
                    if (userId.isNotEmpty()) {
                        // Gerar o link do carrinho usando o userId
                        val cartLink = generateCartLink(userId)

                        // Compartilhar o link gerado
                        shareCartLink(context, cartLink)

                        // Exibir feedback de sucesso
                        Toast.makeText(context, "Carrinho compartilhado!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Alinha no canto inferior direito
                    .padding(16.dp) // Adiciona um espaçamento entre as bordas
            ) {
                Text(text = "Compartilhar")
            }
        }
    }
}
