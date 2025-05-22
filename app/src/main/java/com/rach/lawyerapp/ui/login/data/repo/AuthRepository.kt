package com.rach.lawyerapp.ui.login.data.repo

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {

    val currentUser: MutableStateFlow<FirebaseUser?>
    fun hasVerifiedUser(): Boolean
    fun hasUser(): Boolean
    fun getUserId(): String

    fun sendVerificationLink(onSuccess: () -> Unit, onError: (error: Throwable?) -> Unit)

    suspend fun login(email: String, password: String): Flow<Response<AuthResult?>>
    suspend fun createUser(email: String, password: String): Flow<Response<AuthResult?>>

    suspend fun signWithCredentials(credential:AuthCredential):Flow<Response<AuthResult?>>
    suspend fun forgotPassword(email: String): Flow<Response<Boolean>>


    fun signOut()

}