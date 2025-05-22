package com.rach.lawyerapp.ui.login.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.LoadingViewHorizontal
import com.rach.lawyerapp.ui.login.viewModel.LoginViewModel
import com.rach.lawyerapp.ui.login.viewModel.SignUpViewModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navigateToHomeScreen: () -> Unit,
    isVerificationEmailSent: Boolean,
    forgotPassClicked: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )

    val isLoading = loginViewModel.loginUiState.loading || signUpViewModel.signUiState.isLoading

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        ),
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.padding(dimensionResource(R.dimen.normal_padding)),
                containerColor = Color.Unspecified
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    text = {
                        Text(
                            text = stringResource(R.string.login),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )

                Tab(
                    selected = pagerState.currentPage == 1,
                    text = {
                        Text(
                            text = stringResource(R.string.signup),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.outlined_space)))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> LoginScreen(
                        navigateToHomeScreen = { navigateToHomeScreen() },
                        isVerificationEmailSent = isVerificationEmailSent,
                        forgotPassClicked = forgotPassClicked,
                        signUpTextClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    1 -> SignUpScreen(
                        onNavigateToLoginScreen = { isNavigateToLoginScreen ->
                            scope.launch {
                                if (isNavigateToLoginScreen) {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        },
                        onBackButtonClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        alreadySignUp = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Overlay the loading view on top of the main content
        if (isLoading) {
            LoadingViewHorizontal(
                isLoading = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        MainScreen(
            navigateToHomeScreen = {},
            isVerificationEmailSent = false,
            forgotPassClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}