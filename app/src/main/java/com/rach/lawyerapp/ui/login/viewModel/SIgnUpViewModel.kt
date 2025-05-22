package com.rach.lawyerapp.ui.login.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.lawyerapp.ui.login.data.repo.AuthRepository
import com.rach.lawyerapp.utils.collectAndHandle
import com.rach.lawyerapp.utils.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var signUiState by mutableStateOf(SignUiState())
        private set

    companion object{
        const val TAG = "loginVm"
    }

    fun signUpUiEvents(signUpEvents: SignUpEvents){
        when(signUpEvents){
            is SignUpEvents.OnEmailChange -> signUiState = signUiState.copy(email = signUpEvents.email)
            is SignUpEvents.OnPasswordChange -> signUiState = signUiState.copy(password = signUpEvents.password)
            is SignUpEvents.OnConfirmPasswordChange -> signUiState = signUiState.copy(confirmPass = signUpEvents.confirmPass)
            is SignUpEvents.SignUp -> {
                createUser()
            }

            is SignUpEvents.OnIsEmailVerificationChange ->{
                signUiState = signUiState.copy(
                    isVerificationEmailSent = false
                )
            }
        }
    }

    private fun createUser(){
        viewModelScope.launch {
            try {

                val isNotSamePassword:Boolean = signUiState.password != signUiState.confirmPass

                if (isNotSamePassword) throw IllegalArgumentException("Password do not Match")
                if (!validEmail(signUiState.email)) throw IllegalArgumentException("Invalid Email Address")
                if (!validateSignUpForm()) throw IllegalArgumentException("Field Cannot be Empty")

                signUiState = signUiState.copy(
                    isLoading = true,
                    signUpErrorMessage = null
                )

                repository.createUser(signUiState.email,signUiState.password).collectAndHandle(
                    onError = {
                      signUiState = signUiState.copy(
                            isLoading = false,
                            isSuccessSignUp = false,
                            signUpErrorMessage = it?.localizedMessage
                        )
                    },
                    loading = {
                        signUiState = signUiState.copy(
                            isLoading = true
                        )
                    }
                ) {
                    signUiState = signUiState.copy(
                        isSuccessSignUp = true,
                        isLoading = false
                    )
                    sendEmailVerification()
                }

            }catch (e:Exception){
                signUiState = signUiState.copy(
                    signUpErrorMessage = e.localizedMessage
                )
            }finally {
                signUiState = signUiState.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun sendEmailVerification() = repository.sendVerificationLink(
        onSuccess = {
            signUiState = signUiState.copy(isVerificationEmailSent = true)
        },
        onError = {
            throw it ?: Throwable("Unknown Error")
        }
    )

    private fun validateSignUpForm() = signUiState.run {
        email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()
    }

}


data class SignUiState(
    val email: String = "",
    val password: String = "",
    val confirmPass: String = "",
    val isLoading: Boolean = false,
    val isSuccessSignUp: Boolean = false,
    val isVerificationEmailSent: Boolean = false,
    val signUpErrorMessage: String? = null
)

sealed class SignUpEvents {
    data class OnEmailChange(val email: String) : SignUpEvents()
    data class OnPasswordChange(val password: String) : SignUpEvents()
    data class OnConfirmPasswordChange(val confirmPass: String) : SignUpEvents()
    data object SignUp : SignUpEvents()
    data object OnIsEmailVerificationChange : SignUpEvents()
}