package com.smaple.data.proxy.annotations.methods

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(
    val value:String = ""
)