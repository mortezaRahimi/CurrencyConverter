package com.mortex.converter.utils

import org.jetbrains.annotations.Nullable

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        TOKEN_EXPIRED
    }

    companion object {
        fun <T> success(@Nullable data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> successWithoutBody(msg: String): Resource<T> {
            return Resource(Status.SUCCESS, null, msg)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> tokenExpired(message: String,data: T?) :Resource<T>{
            return Resource(Status.TOKEN_EXPIRED,data, message)
        }
    }
}