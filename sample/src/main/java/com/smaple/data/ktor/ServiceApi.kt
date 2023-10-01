package com.smaple.data.ktor

import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse

interface ServiceApi {
    suspend fun getAllProducts(): ProductResponse

    suspend fun getSingleProduct(productId: Int): Product
}