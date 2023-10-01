package com.smaple.data.repository

import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import com.smaple.data.ktor.ServiceApi


class Repository(private val ktorService: ServiceApi): IRepository {

    override suspend fun getAllProducts(): ProductResponse = ktorService.getAllProducts()
    override suspend fun getSingleProduct(productId: Int): Product = ktorService.getSingleProduct(productId)
}