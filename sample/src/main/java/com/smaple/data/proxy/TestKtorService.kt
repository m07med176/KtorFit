package com.smaple.data.proxy



import android.os.Build
import androidx.annotation.RequiresApi
import com.smaple.data.models.Product
import com.smaple.data.models.ProductResponse
import com.smaple.data.proxy.KtorFitBuilder.create
import com.smaple.data.proxy.annotations.interfaces.Ktorfit
import com.smaple.data.proxy.annotations.methods.GET
import com.smaple.data.proxy.annotations.parameters.Path
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Method

class HttpClientTest

object KtorFitBuilder {
    @RequiresApi(Build.VERSION_CODES.O)
    internal inline fun <reified T : Any>  /* TODO Change to HttpClient*/ HttpClientTest.create(service: Class<T>): T {
        serviceValidation(service)

        val baseUrl = handleBackSlash(service.getAnnotation(Ktorfit::class.java)?.url!!)

        return proxy(contract = service) { method, _ ->
            if (method.isAnnotationPresent(GET::class.java)) {
                var paths = ""
                method.parameters.forEach {parameter ->
                    require(method.annotations.size <=1){ "You should to put only one annotations" }
                    if(parameter.isAnnotationPresent(Path::class.java)){
                        val properties = parameter.javaClass.declaredFields

//                        for (property in properties) {
//                            property.isAccessible = true
//                            val value = property.get(parameter)
//                            println("${property.name}: $value")
//                        }

                        val path = method.getAnnotation(Path::class.java)?.value
//                        paths += if (path != null) {
//                            "/$path"
//                        } else {
//                            "/${parameter.name}"
//                        }
                    }
                }

                val endPoint = method.getAnnotation(GET::class.java)?.value ?: ""

                val urlRequest = "${resolveSlashes(baseUrl)}${resolveSlashes(endPoint)}${resolveSlashes(paths)}"

                println(urlRequest)
                /* TODO Implement Real Ktor Service like: this@create.get(urlRequest) as Product*/
                Product(category = "g")
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




object TestKtorService{
    @JvmStatic
    fun main(array: Array<String>){
        val ktorBuilder = HttpClientTest()
        val service = ktorBuilder.create(ServiceApiKtor::class.java)
        runBlocking {
            val response = service.getSingleProduct(5)

        }
    }

}

// Reflection
// Proxy
// Continuation
// Annotations
// Documentation
// Versions of Libraries
// Licences
// Exceptions

