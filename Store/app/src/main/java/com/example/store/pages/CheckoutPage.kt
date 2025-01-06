package com.example.store.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.store.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    cartViewModel: CartViewModel,
    navController: NavHostController,
) {

    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCVV by remember { mutableStateOf("") }



    val total = cartItems.sumOf { it.totalPrice }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Finalizar Compra",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                            navController.navigate("home")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Ir para Home",
                            tint = Color(0xFF00796B)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFE0F7FA),
                    titleContentColor = Color(0xFF00796B)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Produtos no Carrinho",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color(0xFF444444)
            )

            if (cartItems.isEmpty()) {
                Text("Carrinho vazio", color = Color.Gray, fontSize = 16.sp)
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    cartItems.forEach { item ->
                        var quantity by remember { mutableIntStateOf(item.quantity) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(item.imageUrl),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp)
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                                Text(
                                    text = "Preço: € ${item.price}",
                                    fontSize = 16.sp,
                                    color = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "Preço Total: € ${"%.2f".format(item.price * quantity)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF388E3C)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (quantity > 1) {
                                                quantity -= 1
                                                cartViewModel.updateQuantity(item, quantity)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF388E3C)
                                        ),
                                    ) {
                                        Text("-")
                                    }

                                    Text(
                                        text = "Quantidade: $quantity",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF757575)
                                    )

                                    Button(
                                        onClick = {
                                            quantity += 1
                                            cartViewModel.updateQuantity(item, quantity)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF388E3C)
                                        ),
                                    ) {
                                        Text("+")
                                    }

                                    Button(
                                        onClick = {
                                            cartViewModel.removeItem(item)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFF5252)
                                        ),
                                    ) {
                                        Text("x", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Detalhes do Pagamento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color(0xFF444444)
            )
            PaymentField("Número do Cartão", cardNumber) { cardNumber = it }
            PaymentField("Data de Expiração", cardExpiry) { cardExpiry = it }
            PaymentField("CVV", cardCVV) { cardCVV = it }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Total: € ${"%.2f".format(total)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(
                onClick = {
                    cartViewModel.clearCart()
                    Toast.makeText(context, "Compra finalizada com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    navController.popBackStack()
                    navController.navigate("home")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Finalizar Compra", color = Color.White)
            }
        }
    }
}

@Composable
fun PaymentField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                .padding(16.dp)
        )
    }
}



