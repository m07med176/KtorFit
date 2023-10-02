package com.smaple.data.newProxy

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.coroutines.Continuation

//typealias SuspendInvoker = suspend (method: Method, arguments: List<Any?>) -> Any?
//
//private interface SuspendFunction {
//    suspend fun invoke(): Any?
//}
//
//private val SuspendRemover = SuspendFunction::class.java.methods[0]
//
//@Suppress("UNCHECKED_CAST")
//fun <C : Any> proxy(contract: Class<C>, invoker: SuspendInvoker): C =
//    Proxy.newProxyInstance(contract.classLoader, arrayOf(contract)) { _, method, arguments ->
//        val continuation = arguments.last() as Continuation<*>
//        val argumentsWithoutContinuation = arguments.take(arguments.size - 1)
//        SuspendRemover.invoke(object : SuspendFunction {
//            override suspend fun invoke() = invoker(method, argumentsWithoutContinuation)
//        }, continuation)
//    } as C

interface Adder {
    suspend fun add(a: Int, b: Int): Int
}

//object SuspendProxyTest {
//    @JvmStatic
//    fun main(args: Array<String>)  {
//        val adder = proxy(Adder::class.java) { method, arguments ->
//            println("${method.name}$arguments")
//            delay(100)
//            3
//        }
//        runBlocking {
//           val test =  adder.add(1, 2)
//            println(test)
//        }
//    }
//}
//
//












class CustomException(override val message: String) : RuntimeException(message)

/**
 * Creates an instance of [T] that utilizes our custom [InvocationHandler]
 */
inline fun <reified T : Any> createProxy(
    invocationHandler: InvocationHandler,
): T {
    val service = T::class.java

    return Proxy.newProxyInstance(
        service.classLoader,
        arrayOf(service),
        invocationHandler,
    ) as T
}
