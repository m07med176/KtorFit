package com.smaple.data.repository

import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse

interface IRepository {
    suspend fun getAllProducts(): ProductResponse

    suspend fun getSingleProduct(productId: Int): Product
}