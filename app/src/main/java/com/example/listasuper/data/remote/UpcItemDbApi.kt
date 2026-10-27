package com.example.listasuper.data.remote

import com.example.listasuper.data.remote.dto.UpcItemDbResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UpcItemDbApi {
    @GET("prod/trial/lookup")
    suspend fun lookup(@Query("upc") upc: String): UpcItemDbResponse
}