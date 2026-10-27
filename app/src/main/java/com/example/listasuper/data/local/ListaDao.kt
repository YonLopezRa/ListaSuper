package com.example.listasuper.data.local

import androidx.room.*
import com.example.listasuper.data.local.entities.ListaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDao {
    @Query("SELECT * FROM listas ORDER BY fechaCreacion DESC")
    fun obtenerTodas(): Flow<List<ListaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lista: ListaEntity): Long

    @Update
    suspend fun actualizar(lista: ListaEntity)

    @Delete
    suspend fun eliminar(lista: ListaEntity)

    @Query("SELECT * FROM listas WHERE id = :id")
    suspend fun obtenerPorId(id: Long): ListaEntity?
}