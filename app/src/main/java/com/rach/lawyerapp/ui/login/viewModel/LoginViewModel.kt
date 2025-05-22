package com.rach.lawyerapp.ui.login.viewModel

import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.rach.lawyerapp.ui.login.data.repo.AuthRepository
import com.rach.lawyerapp.ui.login.data.repo.GoogleAuthClient
import com.rach.lawyerapp.utils.collectAndHandle
import com.rach.lawyerapp.utils.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val googleOneTap: GoogleAuthClient
) : ViewModel() {

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    companion object {
        const val TAG = "loginVm"
    }

    fun loginEvents(loginEvents: LoginEvents) {
        when (loginEvents) {
            is LoginEvents.OnEmailChange -> loginUiState =
                loginUiState.copy(email = loginEvents.email)

            is LoginEvents.OnPasswordChange -> loginUiState =
                loginUiState.copy(password = loginEvents.password)

            is LoginEvents.Login -> login()

            is LoginEvents.OnResendVerification -> {
                resendVerification()
            }

            is LoginEvents.SignInWithGoogle -> {
                viewModelScope.launch {
                    googleOneTap.signInWithIntent(loginEvents.intent).collectAndHandle(
                        onError = {
                            loginUiState =
                                loginUiState.copy(loginErrorMessage = it?.localizedMessage)
                        },
                        loading = {
                            loginUiState = loginUiState.copy(loading = true)
                        }
                    ) {
                        hasNotVerifiedThrowError()
                        loginUiState = loginUiState.copy(
                            isSuccessLogin = true,
                            loading = false
                        )
                    }
                }
            }
        }
    }


    private fun validateForm() =
        loginUiState.email.isNotEmpty() && loginUiState.password.isNotEmpty()

    private fun login() {
        viewModelScope.launch {
            try {
                loginUiState = loginUiState.copy(loginErrorMessage = null)
                if (!validateForm()) throw IllegalArgumentException("Password and Email Cannot be Empty")
                if (!validEmail(loginUiState.email)) throw IllegalArgumentException("Invalid Email Address")
                loginUiState = loginUiState.copy(
                    loading = true
                )

                repository.login(loginUiState.email, loginUiState.password).collectAndHandle(
                    onError = { exception ->
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password. Please try again"
                            is FirebaseAuthInvalidUserException -> "User account not found or disabled"
                            else -> exception?.localizedMessage ?: "An unknown error occurred"
                        }
                        loginUiState = loginUiState.copy(
                            isSuccessLogin = false,
                            loading = false,
                            loginErrorMessage = errorMessage
                        )
                    },
                    loading = {
                        loginUiState = loginUiState.copy(
                            loading = true
                        )
                    }
                ) {

                    hasNotVerifiedThrowError()
                    loginUiState = loginUiState.copy(
                        isSuccessLogin = true,
                        loading = false
                    )
                }


            } catch (e: Exception) {
                loginUiState = loginUiState.copy(
                    loginErrorMessage = e.localizedMessage
                )
            } finally {
                loginUiState = loginUiState.copy(
                    loading = false
                )
            }
        }
    }

    private fun hasNotVerifiedThrowError() {
        if (!repository.hasVerifiedUser()) {
            loginUiState = loginUiState.copy(
                showResendButton = true
            )
            throw IllegalArgumentException(
                """
                    We've sent a verification link to your email.
                    Please check your inbox and click the link to activate your account .${loginUiState.currentUser?.email}
                """.trimIndent()
            )
        }
    }

    private fun resendVerification() {
        try {
            repository.sendVerificationLink(
                onSuccess = {
                    loginUiState = loginUiState.copy(
                        showResendButton = false
                    )
                },
                onError = {
                    loginUiState = loginUiState.copy(
                        loginErrorMessage = it?.localizedMessage
                    )
                }
            )
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(
                loginErrorMessage = e.localizedMessage
            )
            e.printStackTrace()
        }
    }

    fun hasUserVerified():Boolean = repository.hasVerifiedUser() && repository.hasUser()
    suspend fun signInWithGoogleIntentSender() :IntentSender? = googleOneTap.signIn()

}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val loginErrorMessage: String? = null,
    val isSuccessLogin: Boolean = false,
    val isValidEmailAddress: Boolean = false,
    val isUserVerified: Boolean? = null,
    val showResendButton: Boolean = false,
    val currentUser: FirebaseUser? = null
)

sealed class LoginEvents {
    data class OnEmailChange(val email: String) : LoginEvents()
    data class OnPasswordChange(val password: String) : LoginEvents()
    data object Login : LoginEvents()
    data object OnResendVerification : LoginEvents()
    data class SignInWithGoogle(val intent: Intent) : LoginEvents()
}