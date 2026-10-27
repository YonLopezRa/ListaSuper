package com.example.listasuper.ui.agregar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listasuper.data.local.AppDatabase
import com.example.listasuper.data.local.entities.ProductoEntity
import com.example.listasuper.data.repository.ProductoRepository
import kotlinx.coroutines.launch

class AgregarProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepo: ProductoRepository

    private val _productoEncontrado = MutableLiveData<ProductoEntity?>()
    val productoEncontrado: LiveData<ProductoEntity?> = _productoEncontrado

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    init {
        val db = AppDatabase.getInstance(application)
        productoRepo = ProductoRepository(db.productoDao())
    }

    fun buscarPorCodigo(codigo: String, listaId: Long) {
        viewModelScope.launch {
            _cargando.value = true
            val producto = productoRepo.buscarPorCodigoBarras(codigo)
            _productoEncontrado.value = producto?.copy(listaId = listaId)
            _cargando.value = false
        }
    }

    fun guardarProducto(nombre: String, marca: String?, cantidad: Int, listaId: Long, codigoBarras: String?, imagenUrl: String?) {
        viewModelScope.launch {
            val producto = ProductoEntity(
                listaId = listaId,
                codigoBarras = codigoBarras,
                nombre = nombre,
                marca = marca,
                imagenUrl = imagenUrl,
                cantidad = cantidad
            )
            productoRepo.agregar(producto)
        }
    }
}