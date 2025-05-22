package com.rach.lawyerapp.ui.login.ui

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.CustomOutLinedTextField
import com.rach.lawyerapp.ui.components.CustomPasswordTextField
import com.rach.lawyerapp.ui.components.LoadingViewHorizontal
import com.rach.lawyerapp.ui.login.viewModel.LoginEvents
import com.rach.lawyerapp.ui.login.viewModel.LoginViewModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navigateToHomeScreen: () -> Unit,
    isVerificationEmailSent: Boolean,
    forgotPassClicked: () -> Unit,
    signUpTextClicked: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val state = viewModel.loginUiState
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    viewModel.loginEvents(
                        loginEvents = LoginEvents.SignInWithGoogle(
                            result.data ?: return@launch
                        )
                    )
                }
            }
        }
    )

    LaunchedEffect(isVerificationEmailSent) {
        if (isVerificationEmailSent) {
            snackbarHostState.showSnackbar(message = "verification email sent to ${state.currentUser?.email}")
        }
    }


    LaunchedEffect(viewModel.hasUserVerified()) {
        if (viewModel.hasUserVerified()) {
            navigateToHomeScreen()
        }

    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier,
            ) {

                AnimatedVisibility(
                    visible = state.loginErrorMessage != null
                ) {
                    Text(
                        text = state.loginErrorMessage ?: "Unknown Error"
                    )
                }


                CustomOutLinedTextField(
                    label = R.string.enter_email,
                    value = state.email,
                    onValueChange = {
                        viewModel.loginEvents(loginEvents = LoginEvents.OnEmailChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.outlined_space)))
                CustomPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = R.string.enter_password,
                    value = state.password,
                    onValueChange = {
                        viewModel.loginEvents(loginEvents = LoginEvents.OnPasswordChange(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

                //Forgot Password
                ForgotText(
                    onClick = { forgotPassClicked() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = dimensionResource(R.dimen.outlined_space))
                )

                // Continue Button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.loginEvents(loginEvents = LoginEvents.Login) },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(R.string.continue_ji),
                        style = MaterialTheme.typography.labelSmall
                    )
                }


                if (state.showResendButton) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.loginEvents(loginEvents = LoginEvents.OnResendVerification) }
                    ) {
                        Text(
                            text = stringResource(R.string.resend)
                        )
                    }
                }
            }

            //Google Button And SignUp Text clicked
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.normal_padding)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginGoogleButton(
                    icon = R.drawable.google,
                    onClick = {
                        scope.launch {
                            val googleIntentSender = viewModel.signInWithGoogleIntentSender()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    googleIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SignText(onClick = signUpTextClicked)
            }
        }

    }


}

@Composable
private fun LoginGoogleButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = { onClick() },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.small_space)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(R.string.login_with_google),
                modifier = Modifier.size(22.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.small_padding)))
            Text(
                text = stringResource(R.string.login_with_google),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun SignText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.did_not_account),
            modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding)),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = stringResource(R.string.signup),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                    indication = rememberRipple(color = MaterialTheme.colorScheme.secondary),
                    interactionSource = remember { MutableInteractionSource() }
                ),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun ForgotText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier.clickable {
            onClick()
        },
        text = stringResource(R.string.forgot_password),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displaySmall
    )
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        LoginScreen(
            navigateToHomeScreen = {},
            isVerificationEmailSent = false,
            forgotPassClicked = {},
            signUpTextClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}

