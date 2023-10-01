package com.smaple.data.ktor

import com.smaple.data.comon.Endpoints
import com.smaple.data.ktor.getUrl
import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorService(private val httpClient: HttpClient): ServiceApi {
    override suspend fun getAllProducts(): ProductResponse
            = httpClient.get(Endpoints.GET_ALL_PRODUCT).body()

    override suspend fun getSingleProduct(productId: Int): Product
            = httpClient.get(Endpoints.GET_SINGLE_PRODUCT.getUrl(productId)).body()
}