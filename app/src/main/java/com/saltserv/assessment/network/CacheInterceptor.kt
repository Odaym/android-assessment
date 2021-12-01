package com.saltserv.assessment.network

import okhttp3.Interceptor

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = try {
        chain.proceed(chain.request())
    } catch (e: Exception) {
        val offlineRequest = chain.request()
            .newBuilder()
            .header("Cache-Control", "public, only-if-cached," + "max-stale=" + (60 * 60))
            .build()
        chain.proceed(offlineRequest)
    }
}