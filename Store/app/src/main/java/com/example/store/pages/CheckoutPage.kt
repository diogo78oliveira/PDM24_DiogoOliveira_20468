package com.example.store.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.store.viewmodel.CartViewModel
import com.example.store.models.Cart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    cartViewModel: CartViewModel,
    navController: NavHostController
) {
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()


    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCVV by remember { mutableStateOf("") }

    // Calculando o total do carrinho
    val total = cartItems.sumByDouble { it.price.toDouble() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
        ) {

            // Título para os produtos no carrinho
            Text(
                text = "Produtos no Carrinho",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Exibindo os itens do carrinho
            if (cartItems.isEmpty()) {
                Text("Carrinho vazio", color = Color.Gray)
            } else {
                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(item.imageUrl),
                            contentDescription = "Imagem do produto",
                            modifier = Modifier.size(60.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = item.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Preço: ${item.price}", fontSize = 16.sp, color = Color.Green)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título para os dados de pagamento
            Text(
                text = "Detalhes do Pagamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Número do cartão
            BasicTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Número do Cartão: ", fontSize = 16.sp)
                        innerTextField() // Campo de entrada
                    }
                }
            )

            // Data de expiração do cartão
            BasicTextField(
                value = cardExpiry,
                onValueChange = { cardExpiry = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Data de Expiração: ", fontSize = 16.sp)
                        innerTextField()
                    }
                }
            )

            // CVV do cartão
            BasicTextField(
                value = cardCVV,
                onValueChange = { cardCVV = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "CVV: ", fontSize = 16.sp)
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Exibindo o total
            Text(
                text = "Total: €$ ${"%.2f".format(total)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Botão para finalizar a compra
            Button(
                onClick = {
                    // Simula a compra
                    cartViewModel.clearCart()  // Limpa
                    // Exibe uma mensagem ou faz outra lógica de finalização
                    println("Compra finalizada com sucesso!")
                    // Adicionar lógica para exibir uma mensagem, ou talvez navegar para outra tela
                    navController.popBackStack()
                    navController.navigate("home")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Finalizar Compra")
            }
        }
    }
}
