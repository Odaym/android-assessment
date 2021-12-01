package com.saltserv.assessment.network

import com.saltserv.assessment.PrefStore
import io.reactivex.Single
import kotlinx.serialization.KSerializer

abstract class BaseNetworkService {
    abstract val processor: NetworkRequestProcessor
    abstract val hostProvider: HostProvider
    abstract val prefStore: PrefStore

    fun get(path: String): NetworkRequestBuilder {
        return NetworkRequestBuilder(hostProvider.host, hostProvider.scheme)
            .withMethod(HttpMethod.GET)
            .withHeader("Authorization", prefStore.getAuthToken())
            .withPath(path)
    }

    protected fun <T : Any> NetworkRequestBuilder.asSingle(serializer: KSerializer<T>): Single<T> {
        return processor.process(this, serializer)
    }
}