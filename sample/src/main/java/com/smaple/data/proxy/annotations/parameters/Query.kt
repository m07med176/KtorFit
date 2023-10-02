package com.smaple.data.proxy.annotations.parameters

@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Query(
    val value:String = ""
)