package com.rach.lawyerapp.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<Response<T>>.collectAndHandle(
    onError:(Throwable?) -> Unit = {
        Log.d("collectAndHandle","Collect And Handle Error",it)
    },
    loading:() -> Unit = {},
    stateReducer:(T) -> Unit
){

    collect{response ->
        when(response){

            is Response.Error -> {
                onError(response.error)
            }

            is Response.Loading ->{
                loading()
            }

            is Response.Success ->{
                stateReducer(response.success)
            }
        }

    }

}

suspend fun <T> Flow<Response<T>>.collectAndHandleSuspend(
    onError: suspend (Throwable?) -> Unit,
    loading: suspend () -> Unit,
    stateReducer:suspend (T) -> Unit
){
   collect{response ->
       when(response){
           is Response.Error ->{
               onError(response.error)
           }

           is Response.Loading ->{
               loading()
           }

           is Response.Success ->
               stateReducer(response.success)
       }
   }
}