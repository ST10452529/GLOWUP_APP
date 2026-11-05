package com.example.glowup_app.data.network

import com.example.glowup_app.data.models.OpenBeautyFactsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenBeautyFactsApi {
    @GET("cgi/search.pl?search_simple=1&action=process&json=1")
    suspend fun searchProducts(
        @Query("search_terms") terms: String,
        @Query("page_size") pageSize: Int = 20
    ): Response<OpenBeautyFactsResponse>
}
