package com.example.listasuper.data.local

import com.example.listasuper.data.local.entities.ListaEntity

data class ListaConEstadisticas(
    val lista: ListaEntity,
    val totalProductos: Int,
    val productosComprados: Int,
    val gastoReal: Double // Suma de precios de los productos
)