package com.example.listasuper.data.local

import androidx.room.*
import com.example.listasuper.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE listaId = :listaId ORDER BY comprado ASC, nombre ASC")
    fun obtenerPorLista(listaId: Long): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE codigoBarras = :codigo LIMIT 1")
    suspend fun buscarPorCodigoBarras(codigo: String): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity): Long

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun eliminar(producto: ProductoEntity)
}