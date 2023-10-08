package com.smaple.data.test

import android.os.Build
import androidx.annotation.RequiresApi
import com.smaple.data.proxy.HttpClientTest
import com.smaple.data.proxy.KtorFitBuilder.create
import com.smaple.data.proxy.ServiceApiKtor
import com.smaple.data.proxy.annotations.parameters.Path
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.internal.impl.metadata.jvm.deserialization.JvmMemberSignature


@RequiresApi(Build.VERSION_CODES.O)
fun main() = runBlocking {

    val ktorBuilder = HttpClientTest()
    val service = ktorBuilder.create(ServiceApiKtor::class.java)

    val result = service.getSingleProduct(5)

//    val method = methods.filter
//        val function:Method? = ServiceApiKtor::class.java.declaredMethods

        val function:Method? = service.javaClass.declaredMethods
        .filterIsInstance<Method>()
        .find { it.name == "getSingleProduct" }


    function?.isAccessible = true

    println("Function: ${function?.name}")

    for (paramere in function?.parameters!!){
        println(paramere.type.instanceOf(Int::class))
    }
//    val productId = function?.parameters?.find { it.isAnnotationPresent(Path::class.java) }?.let { param ->
//
//        param.name
//    }


//    for (method in methods) {


//        method.isAccessible = true
//        val value = method.parameters(person)
//        println("${method.name}: $value")
//    }

//    //val service = ServiceApiKtor::class.java.newInstance()
//    val service = Service()
//    service.getSingleProduct(56)

//val gerAa = function?.annotations?.filter { it.annotationClass == Path::class.java }

//    val productId = function?.parameters?.find { it.isAnnotationPresent(Path::class.java) }?.let { param ->
//        val pathAnnotation = param.getAnnotation(Path::class.java)
//        val parameterName = pathAnnotation.value
//        val parameterValue = 1 // Set the value you want here
//
//        parameterName
//    }
//    println(function?.name)

//    if (productId != null) {
//        val product = service.getSingleProduct(productId)
//        println(product)
//    } else {
//        println("productId not found")
//    }

}