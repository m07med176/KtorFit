package com.smaple.data.proxy.annotations.methods

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DELETE(
    val value:String = ""
)