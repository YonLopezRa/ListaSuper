package com.example.listasuper.data.remote

import com.example.listasuper.data.remote.dto.ProductoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun obtenerProducto(@Path("barcode") barcode: String): ProductoResponse
}