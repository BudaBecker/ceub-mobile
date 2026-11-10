package com.ceub.agenda

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TarefaDbHelper(context: Context) :
    SQLiteOpenHelper(context, "agenda.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE tarefa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                disciplina TEXT NOT NULL,
                descricao TEXT NOT NULL,
                data_entrega TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Sem migrações nesta primeira versão pessoal.
    }

    fun inserir(disciplina: String, descricao: String, dataEntrega: String): Long {
        val valores = ContentValues().apply {
            put("disciplina", disciplina)
            put("descricao", descricao)
            put("data_entrega", dataEntrega)
        }
        return writableDatabase.insert("tarefa", null, valores)
    }

    fun listar(): String {
        val texto = StringBuilder()
        readableDatabase.rawQuery(
            "SELECT id, disciplina, descricao, data_entrega FROM tarefa ORDER BY id",
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                texto.append("ID: ${cursor.getInt(0)} | ${cursor.getString(1)}\n")
                texto.append("Tarefa: ${cursor.getString(2)} | Entrega: ${cursor.getString(3)}\n\n")
            }
        }
        return if (texto.isEmpty()) "Nenhuma tarefa cadastrada." else texto.toString()
    }
}
