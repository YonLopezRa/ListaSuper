package com.example.listasuper.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listas")
data class ListaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val completa: Boolean = false
)