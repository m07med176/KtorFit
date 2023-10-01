package com.smaple.data.retrofit

import com.smaple.data.comon.Endpoints
import com.smaple.data.models.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface NetworkAPI {
    @GET(Endpoints.GET_ALL_PRODUCT_RETRO)
    suspend fun getAllProducts(): Response<ProductResponse>
//
//    @GET(Endpoints.GET_SINGLE_PRODUCT_RETRO)
//    suspend fun getSingleProduct(productId: Int): Response<Product>
}