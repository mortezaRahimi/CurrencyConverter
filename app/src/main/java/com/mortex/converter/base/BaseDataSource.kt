package com.mortex.converter.base

import com.mortex.converter.utils.Resource
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber


abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)
                    return Resource.success(body)
                if (response.code() == 200)
                    return Resource.successWithoutBody("Empty Body")

            }

            if (response.code() == 401)
                return Resource.tokenExpired("Token is Expired",null)

            val jObjError = JSONObject((response.errorBody() as ResponseBody).string())
            val errorMsg = jObjError.get("Message").toString()
            return error(errorMsg)
        } catch (e: Exception) {

            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Timber.d(message)
        return Resource.error(message)
    }


}