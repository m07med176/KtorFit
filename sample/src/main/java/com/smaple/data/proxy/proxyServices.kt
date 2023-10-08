package com.smaple.data.proxy

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.coroutines.Continuation

private interface SuspendFunction {
    suspend fun invoke(): Any?
}

typealias SuspendInvoker = suspend (method: Method, arguments: List<Any?>) -> Any?

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
