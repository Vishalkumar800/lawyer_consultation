package com.rach.lawyerapp.ui.login.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.CustomOutLinedTextField
import com.rach.lawyerapp.ui.components.CustomPasswordTextField
import com.rach.lawyerapp.ui.components.LoadingViewHorizontal
import com.rach.lawyerapp.ui.login.viewModel.SignUpEvents
import com.rach.lawyerapp.ui.login.viewModel.SignUpViewModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme

@Composable
fun SignUpScreen(
    onNavigateToLoginScreen: (Boolean) -> Unit,
    onBackButtonClicked: () -> Unit,
    alreadySignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state = viewModel.signUiState

    LaunchedEffect(state.isVerificationEmailSent) {
        if (state.isVerificationEmailSent) {
            onNavigateToLoginScreen(true)
            viewModel.signUpUiEvents(signUpEvents = SignUpEvents.OnIsEmailVerificationChange)
        }
    }


    BackHandler {
        onBackButtonClicked()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.outlined_space))
        ) {

            AnimatedVisibility(
                visible = state.signUpErrorMessage != null
            ) {
                Text(
                    text = state.signUpErrorMessage ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            //Enter Email
            CustomOutLinedTextField(
                label = R.string.enter_email,
                value = state.email,
                onValueChange = {
                    viewModel.signUpUiEvents(signUpEvents = SignUpEvents.OnEmailChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1
            )

            //Enter Password
            CustomPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                label = R.string.enter_password,
                value = state.password,
                onValueChange = {
                    viewModel.signUpUiEvents(signUpEvents = SignUpEvents.OnPasswordChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            // Enter Confirm Password
            CustomPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                label = R.string.confirm_password,
                value = state.confirmPass,
                onValueChange = {
                    viewModel.signUpUiEvents(signUpEvents = SignUpEvents.OnConfirmPasswordChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            // SignUp Button
            Button(
                onClick = {
                    viewModel.signUpUiEvents(signUpEvents = SignUpEvents.SignUp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.normal_padding)),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(R.string.signup),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        //Already have Account Text
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlreadyAccountText(
                onClick = { alreadySignUp() }
            )
        }
    }

    LoadingViewHorizontal(
        isLoading = state.isLoading
    )

}

@Composable
private fun AlreadyAccountText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.already_account),
            modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding)),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier.clickable(
                onClick = onClick,
                indication = rememberRipple(color = MaterialTheme.colorScheme.secondary),
                interactionSource = remember { MutableInteractionSource() }
            ),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        SignUpScreen(
            onNavigateToLoginScreen = {},
            onBackButtonClicked = {},
            alreadySignUp = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}