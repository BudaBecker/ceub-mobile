package com.example.desafiogamer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaEscuro = darkColorScheme(
    primary = RoxoNeon,
    secondary = AzulNeon,
    background = FundoEscuro,
    surface = CartaoEscuro,
    onPrimary = TextoClaro,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

// O app usa sempre o tema escuro, para combinar com a proposta "gamer".
@Composable
fun DesafioGamerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaEscuro,
        typography = Typography,
        content = content
    )
}
