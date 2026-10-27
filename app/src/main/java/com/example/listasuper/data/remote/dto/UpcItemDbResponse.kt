package com.example.listasuper.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpcItemDbResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("total") val total: Int,
    @SerializedName("items") val items: List<UpcItemDbItem>?
)

data class UpcItemDbItem(
    @SerializedName("ean") val ean: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("brand") val brand: String?,
    @SerializedName("images") val images: List<String>?
)