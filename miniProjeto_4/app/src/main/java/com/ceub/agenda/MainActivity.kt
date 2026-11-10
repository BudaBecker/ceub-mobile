package com.ceub.agenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Minha Agenda de Tarefas", style = MaterialTheme.typography.headlineMedium)
                    val helper = remember { TarefaDbHelper(this@MainActivity) }
                    var disciplina by remember { mutableStateOf("") }
                    var descricao by remember { mutableStateOf("") }
                    var dataEntrega by remember { mutableStateOf("") }
                    var resultado by remember { mutableStateOf("Cadastre ou consulte tarefas.") }

                    OutlinedTextField(
                        value = disciplina,
                        onValueChange = { disciplina = it },
                        label = { Text("Disciplina") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = { Text("Descrição da tarefa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dataEntrega,
                        onValueChange = { dataEntrega = it },
                        label = { Text("Data de entrega") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = {
                        if (disciplina.isBlank() || descricao.isBlank() || dataEntrega.isBlank()) {
                            resultado = "Preencha disciplina, descrição e data de entrega."
                        } else {
                            val id = helper.inserir(disciplina.trim(), descricao.trim(), dataEntrega.trim())
                            resultado = if (id > 0) "Tarefa cadastrada com ID $id" else "Falha ao cadastrar."
                            disciplina = ""
                            descricao = ""
                            dataEntrega = ""
                        }
                    }) {
                        Text("Salvar tarefa")
                    }
                    Button(onClick = { resultado = helper.listar() }) {
                        Text("Consultar tarefas")
                    }
                    Text(resultado)
                }
            }
        }
    }
}
