package com.mortex.converter.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mortex.converter.utils.Resource.Status.*
import kotlinx.coroutines.Dispatchers

fun <T> performGetOperation(
    networkCall: suspend () -> Resource<T>
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())

//        val source = databaseQuery.invoke().map { Resource.success(it) }
//        emitSource(source)

        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            SUCCESS -> {
                emit(Resource.success(responseStatus.data) as Resource<T>)
            }
            ERROR -> {
                emit(Resource.error(responseStatus.message!!,null) as Resource<T>)
            }
            TOKEN_EXPIRED -> {
                emit(Resource.tokenExpired(responseStatus.message!!,null) as Resource<T>)
            }

        }


    }