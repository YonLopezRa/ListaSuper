package com.example.listasuper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductoResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("product") val product: ProductDto?
)

data class ProductDto(
    @SerializedName("product_name") val nombre: String?,
    @SerializedName("brands") val marca: String?,
    @SerializedName("image_url") val imagenUrl: String?,
    @SerializedName("code") val codigo: String?
)