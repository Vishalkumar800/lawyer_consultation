package com.rach.lawyerapp.ui.login.data.repoImp

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.rach.lawyerapp.ui.login.data.repo.GoogleAuthClient
import com.rach.lawyerapp.utils.K
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GoogleAuthClientImp @Inject constructor(
    private val oneTapClient: SignInClient
) : GoogleAuthClient {

    companion object {
        const val TAG = "google_auth"
    }

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }

    override fun signInWithIntent(intent: Intent): Flow<Response<AuthResult?>> = callbackFlow {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(
            googleIdToken,null
        )

        try {
            Firebase.auth.signInWithCredential(googleCredentials)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        trySend(Response.Success(task.result))
                    }else{
                        trySend(Response.Error(task.exception))
                    }
                }

        }catch (e:Exception){
            e.printStackTrace()
        }

        awaitClose {  }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(K.WEB_CLIENT_KEY)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }

}