package com.rach.lawyerapp.navigation.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rach.lawyerapp.R
import com.rach.lawyerapp.navigation.MyAppNavigation
import com.rach.lawyerapp.navigation.Screens
import com.rach.lawyerapp.ui.login.ui.ForgotPasswordScreen
import com.rach.lawyerapp.ui.login.ui.MainScreen

fun NavGraphBuilder.authNavigation(
    navAction: MyAppNavigation,
    navHostController: NavHostController
) {
    navigation(
        startDestination = Screens.Login().routeWithArgs,
        route = Screens.NESTED_AUTH_ROUTE
    ) {
        composable(
            route = Screens.Login().routeWithArgs,
            arguments = listOf(navArgument(name = Screens.IS_EMAIL_SENT_ARGS) {
                defaultValue = false
            })
        ) { entry ->
            MainScreen(
                navigateToHomeScreen = { navAction.navigateToHomeGraph() },
                isVerificationEmailSent = entry.arguments?.getString(Screens.IS_EMAIL_SENT_ARGS)
                    .toBoolean(),
                forgotPassClicked = {
                    navAction.navigateToForgotScreens()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding))
            )
        }

        composable(Screens.AuthenticationScreen.ForgotPassword.route){
            ForgotPasswordScreen()
        }

    }
}