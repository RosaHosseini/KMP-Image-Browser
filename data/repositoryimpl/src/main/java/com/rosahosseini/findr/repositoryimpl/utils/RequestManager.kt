package com.rosahosseini.findr.repositoryimpl.utils

import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import kotlinx.coroutines.flow.*

/**
 * A helper class that supports db request, api requests, and Offline first apis
 * @return a flow of responses
 */
internal class RequestManager<T>(
    private val networkCall: suspend () -> T,
    private val loadFromDb: suspend () -> T?,
    private val saveCallResults: suspend (data: T) -> Unit,
    private val shouldFetch: suspend (data: T?) -> Boolean,
    private val errorManager: ErrorManager
) {
    operator fun invoke(): Flow<Either<T>> {
        return flow {
            emit(Either.Loading())
            // first load from db with state LOADING
            val dbResult: T? = loadFromDb.invoke()?.also { emit(Either.Loading(it)) }
            // if should fetch, fetch from network with state SUCCESS
            if (shouldFetch(dbResult)) fetchFromNetwork()
            // else, send DB data with SUCCESS state
            else if (dbResult != null) emit(Either.Success(dbResult))
            else emit(Either.Error(ErrorModel(ErrorModel.Type.EMPTY_DATA)))
        }.cancellable()
    }

    private suspend fun FlowCollector<Either<T>>.fetchFromNetwork() {
        try {
            val resultData = networkCall()
            // if response was valid send data with state SUCCESS and save it in database
            emit(Either.Loading(resultData))
            saveCallResults.invoke(resultData)
            emit(Either.Success(resultData))
        } catch (e: Exception) {
            // if catch any other error report it with DB data
            emit(Either.Error(errorManager.errorModel(e), loadFromDb.invoke()))
        }
    }

    companion object {
        class Builder<T> {
            private var netWorkCall: (suspend () -> T)? = null
            private var loadFromDb: suspend () -> T? = { null }
            private var saveCallResults: suspend (data: T) -> Unit = {}
            private var shouldFetch: (data: T?) -> Boolean = { true }
            private var errorManager: ErrorManager = RemoteErrorManager()

            fun networkCall(action: suspend () -> T): Builder<T> {
                netWorkCall = action
                return this
            }

            fun loadFromDb(action: suspend () -> T?): Builder<T> {
                loadFromDb = action
                return this
            }

            fun saveCallResults(action: suspend (data: T) -> Unit): Builder<T> {
                saveCallResults = action
                return this
            }

            fun shouldFetch(action: (data: T?) -> Boolean): Builder<T> {
                shouldFetch = action
                return this
            }

            fun setErrorManager(manager: ErrorManager): Builder<T> {
                errorManager = manager
                return this
            }

            fun request(): Flow<Either<T>> {
                checkNotNull(netWorkCall) { "networkCall have not been set!" }
                return RequestManager(
                    requireNotNull(netWorkCall),
                    loadFromDb,
                    saveCallResults,
                    shouldFetch,
                    errorManager
                ).invoke()
            }
        }
    }
}