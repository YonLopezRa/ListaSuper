package com.example.listasuper.data.repository

import com.example.listasuper.data.local.ListaDao
import com.example.listasuper.data.local.entities.ListaEntity
import kotlinx.coroutines.flow.Flow

class ListaRepository(private val listaDao: ListaDao) {

    fun obtenerTodas(): Flow<List<ListaEntity>> = listaDao.obtenerTodas()

    suspend fun crear(nombre: String): Long =
        listaDao.insertar(ListaEntity(nombre = nombre))

    suspend fun actualizar(lista: ListaEntity) = listaDao.actualizar(lista)

    suspend fun eliminar(lista: ListaEntity) = listaDao.eliminar(lista)

    suspend fun obtenerPorId(id: Long) = listaDao.obtenerPorId(id)
}