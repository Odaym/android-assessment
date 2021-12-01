package com.saltserv.assessment.network

import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class NetworkRequestProcessor(
    private val client: OkHttpClient,
    private val json: Json
) {

    fun <T : Any> process(request: NetworkRequest, serializer: KSerializer<T>): Single<T> {
        return singleFromRequest(request, serializer)
    }

    private fun <T : Any> singleFromRequest(
        request: NetworkRequest,
        serializer: KSerializer<T>
    ): Single<T> {
        return Single.create { observer ->
            val call = request.callWithClient(client)

            if (observer.isDisposed) return@create

            call.process(onSuccess = {
                val result = json.decodeFromString(serializer, it.body!!.string())
                observer.onSuccess(result)
            }, onError = {
                throwError(it, observer)
            }, isDisposed = {
                observer.isDisposed
            })

            observer.setCancellable {
                call.cancel()
            }
        }
    }

    private fun NetworkRequest.callWithClient(client: OkHttpClient): Call {
        return client.newCall(
            Request.Builder()
                .url(url)
                .method(method.name, body)
                .apply { headers?.let { headers(it) } }
                .build())
    }

    private fun Call.process(
        onSuccess: (Response) -> Unit,
        onError: (Throwable) -> Unit,
        isDisposed: () -> Boolean
    ) {
        enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                if (call.isCanceled()) return
                onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (isDisposed()) return

                if (response.isSuccessful) {
                    try {
                        onSuccess(response)
                    } catch (e: Exception) {
                        Exceptions.throwIfFatal(e)
                        if (!isDisposed()) {
                            onError(e)
                        }
                    }
                } else {
                    onError(Throwable(response.message))
                }
            }
        })
    }

    private fun <T> throwError(error: Throwable, emitter: SingleEmitter<T>) {
        try {
            emitter.onError(error)
        } catch (e: Exception) {
            Exceptions.throwIfFatal(e)
            RxJavaPlugins.onError(CompositeException(error, e))
        }
    }
}