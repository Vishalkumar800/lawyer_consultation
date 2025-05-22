package com.rach.lawyerapp.utils

sealed class Response<out T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<T> : Response<T>()
    data class Success<out T>(val success: T) : Response<T>(data = success)
    data class Error<out T>(val throwable: Throwable?) : Response<T>(error = throwable)
}

