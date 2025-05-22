package com.rach.lawyerapp.ui.login.data.repo

import android.content.Intent
import android.content.IntentSender
import com.google.firebase.auth.AuthResult
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface GoogleAuthClient {
    suspend fun signIn() : IntentSender?
    fun signInWithIntent(intent: Intent):Flow<Response<AuthResult?>>
}