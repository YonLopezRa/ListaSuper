package com.example.listasuper.ui.detalle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listasuper.data.local.AppDatabase
import com.example.listasuper.data.local.entities.ListaEntity
import com.example.listasuper.data.local.entities.ProductoEntity
import com.example.listasuper.data.repository.ListaRepository
import com.example.listasuper.data.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetalleListaViewModel (application: Application) : AndroidViewModel(application) {

    private val listaRepo: ListaRepository
    private val productoRepo: ProductoRepository

    init {
        val db = AppDatabase.getInstance(application)
        listaRepo = ListaRepository(db.listaDao())
        productoRepo = ProductoRepository(db.productoDao())
    }

    fun obtenerLista(id: Long): Flow<ListaEntity?> = kotlinx.coroutines.flow.flow {
        emit(listaRepo.obtenerPorId(id))
    }

    fun obtenerProductos(listaId: Long): Flow<List<ProductoEntity>> =
        productoRepo.obtenerPorLista(listaId)

    fun toggleComprado(producto: ProductoEntity) = viewModelScope.launch {
        productoRepo.actualizar(producto.copy(comprado = !producto.comprado))
    }

    fun eliminarProducto(producto: ProductoEntity) = viewModelScope.launch {
        productoRepo.eliminar(producto)
    }
}