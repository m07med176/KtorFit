package com.smaple.data.proxy.annotations

import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

fun main(args: Array<String>) {

    val interfaceInvocationHandlerFactoryImpl = InterfaceInvocationHandlerFactoryImpl()


    val serviceWithoutSuspendingFunction = createProxy<ServiceWithoutSuspendingFunction>(
        invocationHandler = interfaceInvocationHandlerFactoryImpl.create(
            object : ServiceWithoutSuspendingFunction {
                override fun add(parameterOne: Int, parameterTwo: Int): Int {
                    return parameterOne + parameterTwo
                }

                override fun exception() {
                    throw CustomException("Error in ServiceWithoutSuspendingFunction")
                }
            }
        )
    )

    val serviceWithSuspendingFunction = createProxy<ServiceWitSuspendingFunction>(
        invocationHandler = interfaceInvocationHandlerFactoryImpl.create(
            object : ServiceWitSuspendingFunction {
                override suspend fun add(parameterOne: Int, parameterTwo: Int): Int {
                    return withContext(Dispatchers.IO) {
                        delay(1000)
                        parameterOne + parameterTwo
                    }
                }

                override suspend fun exception() {
                    throw CustomException("Error in ServiceWitSuspendingFunction")
                }
            }
        )
    )

    runBlocking {
        println("serviceWithoutSuspendingFunction = ${serviceWithoutSuspendingFunction.add(1, 1)}")
        println("serviceWithSuspendingFunction = ${serviceWithSuspendingFunction.add(1, 1)}")

        try {
            serviceWithoutSuspendingFunction.exception()
        } catch (e: Exception) {
            println(e)
        }

        try {
            serviceWithSuspendingFunction.exception()
        } catch (e: Exception) {
            println(e)
        }

    }

}

interface ServiceWithoutSuspendingFunction {
    // adds 2 numbers
    fun add(parameterOne: Int, parameterTwo: Int): Int

    // this will always throw an exception
    fun exception()
}

interface ServiceWitSuspendingFunction {
    // adds 2 numbers
    suspend fun add(parameterOne: Int, parameterTwo: Int): Int

    // this will always throw an exception
    suspend fun exception()
}

class InterfaceInvocationHandlerFactoryImpl : InterfaceInvocationHandlerFactory {

    override fun create(
        originalInterface: Any,
    ): InvocationHandler {
        return object : InvocationHandler {

            override fun invoke(
                proxy: Any,
                method: Method,
                args: Array<out Any>?,
            ): Any? {
                val nonNullArgs = args ?: arrayOf()
                val continuation = nonNullArgs.continuation()

                return if (continuation == null) {
                    // non-suspending function, just invoke regularly
                    try {
                        method.invoke(originalInterface, *nonNullArgs)
                    } catch (invocationTargetException: InvocationTargetException) {
                        throw invocationTargetException.cause ?: invocationTargetException
                    }
                } else {
                    // create a wrapper around the original continuation. we want to do this so we can capture the result and
                    // potentially inspect it
                    val wrappedContinuation = object : Continuation<Any?> {
                        override val context: CoroutineContext get() = continuation.context

                        override fun resumeWith(
                            result: Result<Any?>,
                        ) {
                            // here is where we could inspect result for any type of result / error that we'd like.
                            // since we are not doing anything special with it in this example, we can just resume the continuation
                            // with the value
                            continuation.resumeWith(result)
                        }
                    }

                    invokeSuspendFunction(continuation) outer@{
                        // we want to invoke the method with our continuation wrapper instead
                        // of the original continuation so we can inspect the results. So we will
                        // grab the original arguments, and replace the last element with our continuation wrapper
                        val argumentsWithoutContinuation = if (nonNullArgs.isNotEmpty()) {
                            nonNullArgs.take(nonNullArgs.size - 1)
                        } else {
                            nonNullArgs.toList()
                        }

                        val newArgs = argumentsWithoutContinuation + wrappedContinuation

                        try {
                            val result =
                                method.invoke(
                                    originalInterface,
                                    *newArgs.toTypedArray(),
                                )

                            if (result == COROUTINE_SUSPENDED) {
                                // this can happen if the method we are proxying is a suspending.
                                // when that is the case, just return result / COROUTINE_SUSPENDED since they are the same thing
                                result
                            } else {
                                // here is where we could inspect result. In this
                                result
                            }
                        } catch (invocationTargetException: InvocationTargetException) {
                            throw invocationTargetException.cause ?: invocationTargetException
                        }
                    }
                }
            }


            @Suppress("UNCHECKED_CAST")
            fun <T> invokeSuspendFunction(
                continuation: Continuation<*>,
                block: suspend () -> T,
            ): T =
                (block as (Continuation<*>) -> T)(continuation)


            @Suppress("UNCHECKED_CAST")
            private fun Array<*>?.continuation(): Continuation<Any?>? {
                return this?.lastOrNull() as? Continuation<Any?>
            }

        }
    }
}

/**
 * Factory interface to create [InvocationHandler] around the original interface / service
 */
interface InterfaceInvocationHandlerFactory {
    fun create(
        originalInterface: Any,
    ): InvocationHandler
}

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

class CustomException(override val message: String) : RuntimeException(message)