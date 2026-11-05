package com.example.listasuper.data.repository

import com.example.listasuper.data.local.ListaDao
import com.example.listasuper.data.local.ProductoDao
import com.example.listasuper.data.local.entities.ListaEntity
import kotlinx.coroutines.flow.Flow

class ListaRepository(private val listaDao: ListaDao, private val productoDao: ProductoDao) {

    fun obtenerTodas(): Flow<List<ListaEntity>> = listaDao.obtenerTodas()

    suspend fun crear(nombre: String, tiendaId: String, gastoTotal: Double): Long =
        listaDao.insertar(ListaEntity(nombre = nombre, tienda = tiendaId, gastoTotal = gastoTotal))

    suspend fun actualizar(lista: ListaEntity) = listaDao.actualizar(lista)

    suspend fun eliminar(lista: ListaEntity) = listaDao.eliminar(lista)

    suspend fun obtenerPorId(id: Long) = listaDao.obtenerPorId(id)

    suspend fun obtenerEstadisticas(listaId: Long): Triple<Int, Int, Double> {
        val total = productoDao.contarProductos(listaId)
        val comprados = productoDao.contarComprados(listaId)
        val gasto = productoDao.sumarGastos(listaId) ?: 0.0
        return Triple(total, comprados, gasto)
    }
}