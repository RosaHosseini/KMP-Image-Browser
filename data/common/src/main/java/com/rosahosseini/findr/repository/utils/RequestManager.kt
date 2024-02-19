package com.rosahosseini.findr.repository.utils

import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow

/**
 * A helper class that supports db request, api requests, and Offline first apis
 * @return a flow of responses
 */
class RequestManager<T>(
    private val networkCall: suspend () -> Result<T>,
    private val loadFromDb: suspend () -> T? = { null },
    private val saveCallResults: suspend (data: T) -> Unit = {},
    private val shouldFetch: suspend (data: T?) -> Boolean = { true },
    private val errorManager: ErrorManager = RemoteErrorManager()
) {
    operator fun invoke(): Flow<Either<T>> {
        return flow<Either<T>> {
            emit(Either.Loading())
            // first load from db with state LOADING
            val dbResult: T? = loadFromDb.invoke()?.also { emit(Either.Loading(it)) }
            // if should fetch, fetch from network with state SUCCESS
            if (shouldFetch(dbResult)) {
                networkCall()
                    .onSuccess { data ->
                        // if response was valid send data with state SUCCESS and save it in database
                        emit(Either.Loading(data))
                        saveCallResults.invoke(data)
                        emit(Either.Success(data))
                    }
                    .onFailure {
                        // if catch any other error report it with DB data
                        emit(Either.Error(errorManager.errorModel(it), loadFromDb.invoke()))
                    }
            }
            // else, send DB data with SUCCESS state
            else if (dbResult != null) {
                emit(Either.Success(dbResult))
            } else {
                emit(Either.Error(ErrorModel(ErrorModel.Type.EMPTY_DATA)))
            }
        }.cancellable()
    }
}
