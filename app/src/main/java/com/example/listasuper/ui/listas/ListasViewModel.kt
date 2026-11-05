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

    val listasConEstadisticas = listas.map { listas ->
        listas.map { lista ->
            val stats = repository.obtenerEstadisticas(lista.id)
            ListaConEstadisticas(
                lista = lista,
                totalProductos = stats.first,
                productosComprados = stats.second,
                gastoReal = stats.third
            )
        }
    }.asLiveData()

    fun crearLista(nombre: String, tiendaId: String, gastoTotal: Double) = viewModelScope.launch {
        repository.crear(nombre, tiendaId, gastoTotal)
    }

    fun eliminarLista(lista: ListaEntity) = viewModelScope.launch {
        repository.eliminar(lista)
    }
}