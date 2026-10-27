package com.example.listasuper.data.repository

import com.example.listasuper.data.local.ProductoDao
import com.example.listasuper.data.local.entities.ProductoEntity
import com.example.listasuper.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    fun obtenerPorLista(listaId: Long): Flow<List<ProductoEntity>> =
        productoDao.obtenerPorLista(listaId)

    suspend fun agregar(producto: ProductoEntity) = productoDao.insertar(producto)

    suspend fun actualizar(producto: ProductoEntity) = productoDao.actualizar(producto)

    suspend fun eliminar(producto: ProductoEntity) = productoDao.eliminar(producto)

    /**
     * Busca un producto por código de barras en 3 niveles:
     * 1. Base de datos local (productos que el usuario ya agregó manualmente)
     * 2. Open Food Facts
     * 3. UPCitemdb (respaldo)
     */
    suspend fun buscarPorCodigoBarras(codigo: String): ProductoEntity? {
        // 1. Buscar en local (si el usuario ya lo agregó antes)
        val local = productoDao.buscarPorCodigoBarras(codigo)
        if (local != null) return local

        // 2. Intentar Open Food Facts
        val off = buscarEnOpenFoodFacts(codigo)
        if (off != null) return off

        // 3. Intentar UPCitemdb como respaldo
        val upc = buscarEnUpcItemDb(codigo)
        if (upc != null) return upc

        // 4. Ninguno encontró → null (la UI muestra "completa manualmente")
        return null
    }

    private suspend fun buscarEnOpenFoodFacts(codigo: String): ProductoEntity? {
        return try {
            val response = RetrofitClient.api.obtenerProducto(codigo)
            if (response.status == 1 && response.product != null) {
                ProductoEntity(
                    listaId = 0,
                    codigoBarras = codigo,
                    nombre = response.product.nombre ?: "Producto desconocido",
                    marca = response.product.marca,
                    imagenUrl = response.product.imagenUrl
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun buscarEnUpcItemDb(codigo: String): ProductoEntity? {
        return try {
            val response = RetrofitClient.upcApi.lookup(codigo)
            val item = response.items?.firstOrNull()
            if (item != null && !item.title.isNullOrBlank()) {
                ProductoEntity(
                    listaId = 0,
                    codigoBarras = codigo,
                    nombre = item.title ?: "Producto desconocido",
                    marca = item.brand,
                    imagenUrl = item.images?.firstOrNull()
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}