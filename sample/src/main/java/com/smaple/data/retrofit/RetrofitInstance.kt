package com.smaple.data.retrofit

import com.smaple.data.comon.Endpoints
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .client(cashAndLoggerManager())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NetworkAPI by lazy {
        retrofit.create(NetworkAPI::class.java)
    }

    private fun cashAndLoggerManager(): OkHttpClient {
        // Logging Retrofit
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
}