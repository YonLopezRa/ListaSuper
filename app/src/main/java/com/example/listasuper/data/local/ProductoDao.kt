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

    @Query("SELECT COUNT(*) FROM productos WHERE listaId = :listaId")
    suspend fun contarProductos(listaId: Long): Int

    @Query("SELECT COUNT(*) FROM productos WHERE listaId = :listaId AND comprado = 1")
    suspend fun contarComprados(listaId: Long): Int

    @Query("SELECT SUM(precio) FROM productos WHERE listaId = :listaId")
    suspend fun sumarGastos(listaId: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity): Long

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun eliminar(producto: ProductoEntity)
}