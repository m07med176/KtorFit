package com.smaple.data.test

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


//object ProxyExample {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        // Define an interface
//        val realObject: MyInterface = RealObject()
//
//        // Create an invocation handler
//        val handler: InvocationHandler = MyInvocationHandler(realObject)
//
//        // Create a dynamic proxy instance
//        val proxy = Proxy.newProxyInstance(
//            MyInterface::class.java.classLoader, arrayOf<Class<*>>(MyInterface::class.java),
//            handler
//        ) as MyInterface
//
//        // Use the dynamic proxy
//        proxy.doSomething()
//    }
//}


//
//object ProxyDemo {
//    @Throws(IllegalArgumentException::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val handler: InvocationHandler = SampleInvocationHandler()
//
//        val proxy = Proxy.newProxyInstance(
//            SampleInterface::class.java.classLoader,
//            arrayOf<Class<*>>(SampleInterface::class.java),
//            handler
//        ) as SampleInterface
//
//
//        proxy.showMessage()
//
//        //val invocationHandler: Class<*> = Proxy.getInvocationHandler(proxy).javaClass
//        //println(invocationHandler.name)
//    }
//}
//
//internal class SampleInvocationHandler : InvocationHandler {
//    @Throws(Throwable::class)
//    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
//        println("Welcome to TutorialsPoint")
//        return null
//    }
//}
//internal class MyInvocationHandler(private val realObject: MyInterface) : InvocationHandler {
//    @Throws(Throwable::class)
//    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
//        println("Before method call")
//
//        return method.invoke(realObject)
//    }
//}
//
//
//
//internal interface SampleInterface {
//    fun showMessage()
//}
//internal class SampleClass : SampleInterface {
//    override fun showMessage() {
//        println("Hello World")
//    }
//}
//
//
//internal interface MyInterface {
//    fun doSomething():String
//}
//internal class RealObject : MyInterface {
//    override fun doSomething():String {
//        println("RealObject is doing something.")
//        return "sfs"
//    }
//}
//
//
//class Person2(private val name: String) : IPerson {
//    override fun say() {
//        println("Person:$name")
//    }
//}
//interface IPerson {
//    fun say()
//}
//internal class MyHandler(private val theobj: Any) : InvocationHandler {
//    @Throws(Throwable::class)
//    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any { //
//        println("before")
//        return method.invoke(theobj, *args)
//    }
//}
//object TestProxy {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val p = Person2("myName")
//        val invocationHandler: InvocationHandler = MyHandler(p)
//        val obj = Proxy.newProxyInstance(
//            p.javaClass.classLoader,
//            p.javaClass.interfaces,
//            invocationHandler
//        ) as IPerson
//
//        obj.say()
//    }
//}
//
