package com.smaple.data.proxy.annotations

import retrofit2.http.Url
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class GET(
    val value:String = ""
)