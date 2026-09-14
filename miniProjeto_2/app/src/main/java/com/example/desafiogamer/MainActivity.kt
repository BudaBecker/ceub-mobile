package com.example.desafiogamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desafiogamer.ui.theme.Bronze
import com.example.desafiogamer.ui.theme.DesafioGamerTheme
import com.example.desafiogamer.ui.theme.Ouro
import com.example.desafiogamer.ui.theme.Prata
import com.example.desafiogamer.ui.theme.VermelhoGameOver
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesafioGamerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaScoreGamer()
                }
            }
        }
    }
}

// Constantes do cálculo (val = valor fixo, não muda durante a execução).
const val PONTOS_POR_INIMIGO = 100
const val PONTOS_POR_MOEDA = 1

@Composable
fun TelaScoreGamer() {
    // ------------------------------------------------------------------
    // ESTADOS DA TELA
    // "remember + mutableStateOf" guardam o valor digitado e avisam a tela
    // sempre que esse valor mudar, para o Compose desenhar tudo de novo.
    // ------------------------------------------------------------------
    var nome by remember { mutableStateOf("") }
    var inimigos by remember { mutableStateOf("") }
    var moedas by remember { mutableStateOf("") }
    var vidas by remember { mutableStateOf("") }

    // Estados do resultado, preenchidos quando o botão é clicado.
    var mostrarResultado by remember { mutableStateOf(false) }
    var nomeJogador by remember { mutableStateOf("") }
    var pontuacao by remember { mutableStateOf(0) }
    var ranking by remember { mutableStateOf("") }
    var gameOver by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            // imePadding evita que o teclado cubra o conteúdo da tela.
            .imePadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------------------- CABEÇALHO ----------------------
        Text(
            text = "🎮 CALCULADORA DE SCORE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Informe os dados da partida e descubra o seu ranking",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------------------- FORMULÁRIO ----------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = nome,
                    onValueChange = { novoTexto -> nome = novoTexto },
                    label = { Text("Nome do jogador") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Row = dois campos lado a lado.
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = inimigos,
                        // filter { it.isDigit() } permite digitar apenas números.
                        onValueChange = { novoTexto -> inimigos = novoTexto.filter { it.isDigit() } },
                        label = { Text("Inimigos") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    TextField(
                        value = moedas,
                        onValueChange = { novoTexto -> moedas = novoTexto.filter { it.isDigit() } },
                        label = { Text("Moedas") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = vidas,
                    onValueChange = { novoTexto -> vidas = novoTexto.filter { it.isDigit() } },
                    label = { Text("Vidas restantes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------------- BOTÕES ----------------------
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    // Converte o texto digitado em número.
                    // toIntOrNull() devolve null se o campo estiver vazio ou inválido.
                    val qtdInimigos = inimigos.toIntOrNull() ?: -1
                    val qtdMoedas = moedas.toIntOrNull() ?: -1
                    val qtdVidas = vidas.toIntOrNull() ?: -1

                    if (nome.isBlank()) {
                        mensagemErro = "Digite o nome do jogador."
                        mostrarResultado = false
                    } else if (qtdInimigos < 0 || qtdMoedas < 0 || qtdVidas < 0) {
                        mensagemErro = "Preencha inimigos, moedas e vidas (use 0 se não tiver)."
                        mostrarResultado = false
                    } else {
                        mensagemErro = ""

                        // CÁLCULO: (inimigos × 100) + moedas
                        pontuacao = (qtdInimigos * PONTOS_POR_INIMIGO) + (qtdMoedas * PONTOS_POR_MOEDA)

                        // RANKING: if / else if / else
                        if (pontuacao >= 2000) {
                            ranking = "RANK OURO"
                        } else if (pontuacao >= 1000) {
                            ranking = "RANK PRATA"
                        } else {
                            ranking = "RANK BRONZE"
                        }

                        // DESAFIO ADICIONAL: 0 vidas = GAME OVER!
                        gameOver = qtdVidas == 0

                        nomeJogador = nome
                        mostrarResultado = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(text = "CALCULAR SCORE", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = {
                    // Limpa todos os campos e esconde o resultado.
                    nome = ""
                    inimigos = ""
                    moedas = ""
                    vidas = ""
                    mensagemErro = ""
                    mostrarResultado = false
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.height(52.dp)
            ) {
                Text(text = "LIMPAR")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------------- MENSAGEM DE ERRO ----------------------
        if (mensagemErro != "") {
            Text(
                text = mensagemErro,
                color = VermelhoGameOver,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ---------------------- RESULTADO ----------------------
        if (mostrarResultado) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "RESULTADO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Jogador: " + nomeJogador,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // %,d coloca o separador de milhar (exemplo: 1.800).
                    Text(
                        text = String.format(Locale.forLanguageTag("pt-BR"), "%,d pontos", pontuacao),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = ranking,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = corDoRanking(ranking)
                    )

                    // DESAFIO ADICIONAL: só aparece quando as vidas são 0.
                    if (gameOver) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "GAME OVER!",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = VermelhoGameOver
                        )
                        Text(
                            text = "O jogador terminou com 0 vidas.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cálculo: (inimigos × 100) + moedas",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Ouro: 2.000+  •  Prata: 1.000 a 1.999  •  Bronze: abaixo de 1.000",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Escolhe a cor do texto do ranking (if / else if / else).
fun corDoRanking(ranking: String): Color {
    return if (ranking == "RANK OURO") {
        Ouro
    } else if (ranking == "RANK PRATA") {
        Prata
    } else {
        Bronze
    }
}

@Preview(showBackground = true)
@Composable
fun TelaScoreGamerPreview() {
    DesafioGamerTheme {
        TelaScoreGamer()
    }
}
