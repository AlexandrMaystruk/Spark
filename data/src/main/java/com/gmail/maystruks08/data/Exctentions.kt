package com.gmail.maystruks08.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun <T, A> resultLiveData(databaseQuery: () -> Flow<T>,
                          networkCall: suspend () -> Result<A>,
                          saveCallResult: suspend (A?) -> Unit): Flow<Result<T>> =
    flow {




    }
//    liveData(Dispatchers.IO) {
//        emit(Result.loading())
//        val source = databaseQuery.invoke().map { Result.success(it) }
//        emitSource(source)
//
//        val responseStatus = networkCall.invoke()
//        if (responseStatus.status == Result.Status.SUCCESS) {
//            saveCallResult(responseStatus.data)
//        } else if (responseStatus.status == Result.Status.ERROR) {
//            emit(Result.error<T>(responseStatus.message ?: "Unknown error"))
//            emitSource(source)
//        }
//    }