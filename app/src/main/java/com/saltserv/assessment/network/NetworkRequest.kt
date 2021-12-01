package com.saltserv.assessment.network

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.RequestBody

enum class HttpMethod {
    GET,
    POST
}

interface NetworkRequest {
    val url: HttpUrl
    val method: HttpMethod
    val headers: Headers?
    val body: RequestBody?
}

class NetworkRequestBuilder(
    private val host: String,
    private val scheme: String
) : NetworkRequest {

    private class QueryItem(val name: String, val value: Any?)

    override val url: HttpUrl
        get() = HttpUrl.Builder()
            .host(host)
            .scheme(scheme)
            .encodedPath(if (path.startsWith("/")) path else "/$path")
            .apply { params?.forEach { addQueryParameter(it.name, it.value?.toString()) } }
            .build()

    override var method: HttpMethod = HttpMethod.GET
        private set

    override val headers: Headers?
        get() = headersBuilder?.build()

    override var body: RequestBody? = null
        private set

    private var path: String = ""

    private var params: MutableList<QueryItem>? = null

    private var headersBuilder: Headers.Builder? = null

    fun withMethod(method: HttpMethod): NetworkRequestBuilder {
        this.method = method
        return this
    }

    fun withHeader(name: String, value: String): NetworkRequestBuilder {
        if (headersBuilder == null) {
            headersBuilder = Headers.Builder()
        }
        headersBuilder?.add(name, value)
        return this
    }

    fun withPath(path: String): NetworkRequestBuilder {
        this.path = path
        return this
    }

    fun withParam(name: String, value: Any?): NetworkRequestBuilder {
        if (value == null) return this
        if (params == null) {
            params = mutableListOf()
        }
        params?.add(
            QueryItem(
                name,
                value
            )
        )
        return this
    }
}