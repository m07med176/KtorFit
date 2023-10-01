package com.smaple.data.proxy

import com.smaple.data.comon.Endpoints
import com.smaple.data.ktor.ServiceApi
import com.smaple.data.ktor.getUrl
import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import com.smaple.data.proxy.KtorFitBuilder.create
import com.smaple.data.proxy.annotations.GET
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


object KtorFitBuilder{
    fun createHttpClient( enableNetworkLogs: Boolean = true):HttpClient =
        HttpClient(OkHttp) {
            defaultRequest {
                header("Content-Type", "application/json")
                header("Accept", "application/json")
            }

            // exception handling
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }

            install(HttpRequestRetry) {
                maxRetries = 3
                retryIf { _, response -> !response.status.isSuccess() }
                retryOnExceptionIf { _, cause -> cause is HttpRequestTimeoutException }
                delayMillis { 3000L } // retries in 3, 6, 9, etc. seconds
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { cause, _ -> println("exception $cause") }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                        prettyPrint = true
                        coerceInputValues = true
                    }
                )
            }
            if (enableNetworkLogs) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
        }


    internal inline fun <reified T> HttpClient.create(service: Class<T>): T {
        serviceValidation(service)
        return Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf<Class<*>>(T::class.java),
        ) { proxy, method, args ->

            method.annotations.forEach { _ ->
                if (method.isAnnotationPresent(GET::class.java)) {
                    val endPoint = method.getAnnotation(GET::class.java)
                    endPoint?.let {
                        runBlocking {
                            this@create.get(Endpoints.BASE_URL + "/" + endPoint.value)
                                .body() as ProductResponse
                        }
                    }
                }
            }
            runBlocking {
                this@create.get(Endpoints.GET_ALL_PRODUCT).body() as ProductResponse

            }
        } as T
    }

    internal fun serviceValidation(service:Class<*>) {
        require(service.isInterface){"You should to use interface class"}
    }
}

interface ServiceApiNew {

    @GET(Endpoints.GET_ALL_PRODUCT_RETRO)
    fun getAllProducts(): ProductResponse

}



object ImplementationTest {
    @JvmStatic
    fun main(args: Array<String>) {

        val proxy = KtorFitBuilder.createHttpClient().create(ServiceApiNew::class.java)
        val data  = proxy.getAllProducts()
        println(data)
    }
}












//        val invocationHandler: Class<*> = Proxy.getInvocationHandler(proxy).javaClass
//        println(invocationHandler.name)
