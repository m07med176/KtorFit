package com.smaple.data.proxy


import android.os.Build
import androidx.annotation.RequiresApi
import com.smaple.data.comon.Endpoints
import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import com.smaple.data.proxy.annotations.interfaces.Ktorfit
import com.smaple.data.proxy.annotations.methods.GET
import com.smaple.data.proxy.annotations.parameters.Path
import io.ktor.client.HttpClient
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
import kotlinx.serialization.json.Json
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.coroutines.Continuation


object KtorFitBuilder {
    fun createHttpClient( enableNetworkLogs: Boolean = true):HttpClient =
        HttpClient(OkHttp) {
            defaultRequest {
                header("Content-Type", "application/json")
                header("Accept", "application/json")
            }

            install(HttpTimeout) {
                requestTimeoutMillis    = 10000
                connectTimeoutMillis    = 10000
                socketTimeoutMillis     = 10000
            }

            install(HttpRequestRetry) {
                maxRetries = 3
                retryIf { _, response -> !response.status.isSuccess() }
                retryOnExceptionIf { _, cause -> cause is HttpRequestTimeoutException }
                delayMillis { 3000L }
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

    @RequiresApi(Build.VERSION_CODES.O)
    internal inline fun <reified T : Any> HttpClient.create(service: Class<T>): T {
        serviceValidation(service)

        val baseUrl = handleBackSlash(service.getAnnotation(Ktorfit::class.java)?.url!!)

        return proxy(contract = service) { method, _ ->
            if (method.isAnnotationPresent(GET::class.java)) {
                var paths = ""
                method.parameters.forEach {
                    require(method.annotations.size <=1){ "You should to put only one annotations" }
                    if(it.isAnnotationPresent(Path::class.java)){
                        val path = method.getAnnotation(Path::class.java)?.value
                        paths += if (path != null) {
                            "/$path"
                        } else {
                            "/${it.name}"
                        }
                    }
                }

                val endPoint = method.getAnnotation(GET::class.java)?.value ?: ""

                val urlRequest = "${resolveSlashes(baseUrl)}${resolveSlashes(endPoint)}${resolveSlashes(paths)}"
                this@create.get(urlRequest) as Product
            }else{
                Product(category = "g")
            }

        }
    }
    internal fun requestValidation(method:Method) {
        require(method.returnType.name == ProductResponse::class.java.name){"You fdshgfhg54 654 s"}
    }

    internal fun serviceValidation(service:Class<*>) {
        require(service.isInterface){"You should to use interface class"}
        require(service.isAnnotationPresent(Ktorfit::class.java)){"You should to put annotation of `ktorfit` at top of Interface "}
        require(isValidURL(service.getAnnotation(Ktorfit::class.java)?.url)){"You should to put valid URL in `ktorfit` annotation "}
    }
}



typealias SuspendInvoker = suspend (method: Method, arguments: List<Any?>) -> Any?

private interface SuspendFunction {
    suspend fun invoke(): Any?
}

private val SuspendRemover = SuspendFunction::class.java.methods[0]

@Suppress("UNCHECKED_CAST")
fun <T : Any> proxy(contract: Class<T>, invoker: SuspendInvoker): T =
    Proxy.newProxyInstance(contract.classLoader, arrayOf(contract)) { _, method, arguments ->
        val continuation = arguments.last() as Continuation<*>
        val argumentsWithoutContinuation = arguments.take(arguments.size - 1)
        SuspendRemover.invoke(object : SuspendFunction {
            override suspend fun invoke() = invoker(method, argumentsWithoutContinuation)
        }, continuation)
    } as T


// Reflection
// Proxy
// Continuation
// Annotations
// Documentation
// Versions of Libraries
// Licences
// Exceptions


fun resolveSlashes(input: String):String =
    removeLeadingSlash(input)
        .apply { removeExtraSlashes(input) }
        .apply { handleBackSlash(this) }
fun removeExtraSlashes(input: String): String  = input.replace(Regex("/+"), "/")

fun isValidURL(url: String?): Boolean {
    if (url==null) return false
    val regex = """^(https?)://[^\s/$.?#].[^\s]*$""".toRegex()
    return regex.matches(url)
}
fun removeLeadingSlash(input: String): String {
    return input.removePrefix("/")
}



private fun handleBackSlash(url: String): String {
    return if (!url.endsWith("/")) {
        "$url/"
    } else {
        url
    }
}
fun main() {
    val urls = listOf(
        "https://dummyjson.com",
        "ftp://example.com",
        "http://localhost:8080/",
        "invalid-url",
        "https://with spaces.com"
    )


    for (url in urls) {
        val isValid = isValidURL(url)
        if (isValid){

        println("$url is valid: ${handleBackSlash(url)}")
        }
    }


    val input1 = "/example/path"
    val input2 = "noLeadingSlash"

    val result1 = removeLeadingSlash(input1)
    val result2 = removeLeadingSlash(input2)

    println(result1) // Output: "example/path"
    println(result2) // Output: "noLeadingSlash"
}