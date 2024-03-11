package com.rosahosseini.findr.data.network

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit

internal class KotlinResultCallAdapterFactory(
    private val errorManager: RemoteErrorManager
) : CallAdapter.Factory() {
    @Suppress("ReturnCount")
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != Result::class.java) {
            return null
        }

        val responseType = getParameterUpperBound(0, callType as ParameterizedType)
        return ResultCallAdapter(responseType = responseType)
    }

    private inner class ResultCallAdapter(
        private val responseType: Type
    ) : CallAdapter<Type, Call<Result<Type>>> {
        override fun responseType(): Type = responseType
        override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(call = call)
    }

    private inner class ResultCall<T : Any>(private val call: Call<T>) : Call<Result<T>> {
        override fun enqueue(callback: Callback<Result<T>>) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val result = response.toResult()
                    callback.onResponse(this@ResultCall, Response.success(result))
                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    val apiError = errorManager.apiError(throwable = throwable)
                    val result = Result.failure<T>(exception = apiError)
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            })
        }

        override fun execute(): Response<Result<T>> = throw NotImplementedError()
        override fun clone(): Call<Result<T>> = ResultCall(call.clone())
        override fun request(): Request = call.request()
        override fun timeout(): Timeout = call.timeout()
        override fun isExecuted(): Boolean = call.isExecuted
        override fun isCanceled(): Boolean = call.isCanceled
        override fun cancel() = call.cancel()

        @Suppress("UNCHECKED_CAST")
        private fun <T> Response<T>.toResult(): Result<T> {
            val body = body()
            return when {
                isSuccessful && body == null -> Result.success(Unit as T)
                isSuccessful && body != null -> Result.success(body)
                else -> Result.failure(errorManager.apiError(HttpException(this)))
            }
        }
    }
}
