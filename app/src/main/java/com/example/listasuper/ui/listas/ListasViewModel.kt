package com.example.listasuper.ui.listas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.listasuper.data.local.AppDatabase
import com.example.listasuper.data.local.ListaConEstadisticas
import com.example.listasuper.data.local.entities.ListaEntity
import com.example.listasuper.data.repository.ListaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListaRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ListaRepository(db.listaDao(),db.productoDao())
    }

    val listas: Flow<List<ListaEntity>> = repository.obtenerTodas()

    val listasConEstadisticas = repository.obtenerTodasConProductos().map { listas ->
        listas.map { item ->
            val totalProductos = item.productos.size
            val productosComprados = item.productos.count { it.comprado }
            val totalPiezas = item.productos.sumOf { it.cantidad }
            val piezasCompradas = item.productos.filter { it.comprado }.sumOf { it.cantidad }
            val gastoReal = item.productos.sumOf { (it.precio) * it.cantidad }

            ListaConEstadisticas(
                lista = item.lista,
                totalProductos = totalProductos,
                productosComprados = productosComprados,
                totalPiezas = totalPiezas,
                piezasCompradas = piezasCompradas,
                gastoReal = gastoReal
            )
        }
    }.asLiveData()

    val ahorroInteligente = repository.obtenerTodas().map { listas ->
        // Tomamos las 2 listas más recientes (ya vienen ordenadas por fecha)
        if (listas.size >= 2) {
            val listaActual = listas[0]
            val listaAnterior = listas[1]

            val gastoActual = repository.obtenerGastoTotal(listaActual.id)
            val gastoAnterior = repository.obtenerGastoTotal(listaAnterior.id)

            gastoAnterior - gastoActual // Positivo = ahorro, negativo = gastaste más
        } else {
            0.0
        }
    }.asLiveData()

    fun crearLista(nombre: String, tiendaId: String) = viewModelScope.launch {
        repository.crear(nombre, tiendaId)
    }

    fun eliminarLista(lista: ListaEntity) = viewModelScope.launch {
        repository.eliminar(lista)
    }
}