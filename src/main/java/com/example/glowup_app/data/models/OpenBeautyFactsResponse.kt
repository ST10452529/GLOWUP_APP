package com.example.glowup_app.data.models

import com.google.gson.annotations.SerializedName

data class OpenBeautyFactsResponse(
    val count: Int = 0,
    val products: List<ApiProduct>? = emptyList()
)

data class ApiProduct(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("product_name") val productName: String? = null,
    @SerializedName("brands") val brands: String? = null,
    @SerializedName("categories") val categories: String? = null,
    @SerializedName("image_front_small_url") val imageUrlSmall: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null
)
