package com.example.listasuper.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    foreignKeys = [
        ForeignKey(
            entity = ListaEntity::class,
            parentColumns = ["id"],
            childColumns = ["listaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listaId: Long,
    val codigoBarras: String? = null,
    val nombre: String,
    val marca: String? = null,
    val imagenUrl: String? = null,
    val cantidad: Int = 1,
    val comprado: Boolean = false
)