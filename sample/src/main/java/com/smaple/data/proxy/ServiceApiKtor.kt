package com.smaple.data.proxy

import com.smaple.data.comon.Endpoints
import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import com.smaple.data.proxy.annotations.interfaces.Ktorfit
import com.smaple.data.proxy.annotations.methods.GET
import com.smaple.data.proxy.annotations.parameters.Path
import com.smaple.data.proxy.annotations.parameters.Query


@Ktorfit(url = Endpoints.BASE_URL)
interface ServiceApiKtor {

    @GET(Endpoints.GET_ALL_PRODUCT_RETRO)
    suspend fun getAllProducts(): ProductResponse

    @GET(Endpoints.GET_SINGLE_PRODUCT_RETRO)
    suspend fun getSingleProduct(@Path productId: Int): Product
}