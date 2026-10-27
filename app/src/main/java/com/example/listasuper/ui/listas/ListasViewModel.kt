package com.example.listasuper.ui.listas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listasuper.data.local.AppDatabase
import com.example.listasuper.data.local.entities.ListaEntity
import com.example.listasuper.data.repository.ListaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class ListasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ListaRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ListaRepository(db.listaDao())
    }

    val listas: Flow<List<ListaEntity>> = repository.obtenerTodas()

    fun crearLista(nombre: String) = viewModelScope.launch {
        repository.crear(nombre)
    }

    fun eliminarLista(lista: ListaEntity) = viewModelScope.launch {
        repository.eliminar(lista)
    }
}