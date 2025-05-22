package com.rach.lawyerapp.ui.login.data.repoImp

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rach.lawyerapp.ui.login.data.repo.AuthRepository
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: MutableStateFlow<FirebaseUser?> =
        MutableStateFlow(firebaseAuth.currentUser)

    override fun hasVerifiedUser(): Boolean {
        return firebaseAuth.currentUser?.isEmailVerified ?: false

    }

    override fun hasUser(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUserId(): String {
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    override fun sendVerificationLink(onSuccess: () -> Unit, onError: (error: Throwable?) -> Unit) {
        firebaseAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.cause)
                }
            }
    }

    override suspend fun login(email: String, password: String): Flow<Response<AuthResult?>> =
        flow {
            emit(Response.Loading())
            try {

                val task = withContext(Dispatchers.IO) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                }
                currentUser.value = task.user
                emit(Response.Success(task))

            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun createUser(email: String, password: String): Flow<Response<AuthResult?>> =
        callbackFlow {
            try {
                trySend(Response.Loading())
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            currentUser.value = task.result.user
                            trySend(Response.Success(task.result))
                        } else {
                            trySend(Response.Error(task.exception))
                        }
                    }

            } catch (e: Exception) {
                trySend(Response.Error(e))
            }
            awaitClose { }

        }

    override suspend fun signWithCredentials(credential: AuthCredential): Flow<Response<AuthResult?>> = callbackFlow {
        try {
            trySend(Response.Loading())
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Response.Success(task.result)
                    }else{
                        Response.Error(task.exception)
                    }
                }

        }catch (e:Exception){
            trySend(Response.Error(e))
        }
        awaitClose {  }

    }

    override suspend fun forgotPassword(email: String): Flow<Response<Boolean>> = callbackFlow {
        try {
            trySend(Response.Loading())
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Response.Success(task.result)
                    } else {
                        Response.Error(task.exception)
                    }
                }

        } catch (e: Exception) {
            trySend(Response.Error(e))
        }

        awaitClose { }
    }

    override fun signOut() {
        firebaseAuth.signOut()
        currentUser.value = null
    }

}