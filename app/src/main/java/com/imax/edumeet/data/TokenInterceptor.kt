package com.imax.edumeet.data

import com.imax.edumeet.utils.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var localStorage: LocalStorage
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${localStorage.token}"
            )
            .build()
        return chain.proceed(request)
    }
}