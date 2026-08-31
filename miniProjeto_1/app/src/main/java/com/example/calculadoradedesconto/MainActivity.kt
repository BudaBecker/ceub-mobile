package com.example.calculadoradedesconto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadoradedesconto.ui.theme.CalculadoraDeDescontoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraDeDescontoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculadoraDesconto(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculadoraDesconto(modifier: Modifier = Modifier) {
    // "estado" da tela: quando esses valores mudam, a UI se redesenha sozinha.
    // mutableStateOf("") -> trackeado pelo compose
    var precoTexto by remember { mutableStateOf("") }
    var descontoTexto by remember { mutableStateOf("") }
    var precoFinal by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Escrita simples
        Text(
            text = "Calculadora de Desconto",
            style = MaterialTheme.typography.headlineSmall
        )

        // Primeira text box
        OutlinedTextField(
            value = precoTexto,
            onValueChange = { precoTexto = it },
            label = { Text("Preço original (R$)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Segunda text box
        OutlinedTextField(
            value = descontoTexto,
            onValueChange = { descontoTexto = it },
            label = { Text("Desconto (%)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Botao calcular
        Button(
            onClick = {
                val preco = precoTexto.toDoubleOrNull()
                val desconto = descontoTexto.toDoubleOrNull()

                if (preco != null && desconto != null) {
                    precoFinal = preco - (preco * desconto / 100.0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }

        if (precoFinal != null){
            Text(
                text = "Preço final: R$ %.2f".format(precoFinal)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraDescontoPreview() {
    CalculadoraDeDescontoTheme {
        CalculadoraDesconto()
    }
}