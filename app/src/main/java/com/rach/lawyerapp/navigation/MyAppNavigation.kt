package com.rach.lawyerapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class MyAppNavigation(
    val navController: NavController
){
    val navigateToForgotScreens:() -> Unit ={
        navController.navigateToSingleTop(Screens.AuthenticationScreen.ForgotPassword.route)
    }

    val navigateToLoginScreenWithArgs:(isEmailVerified:Boolean) -> Unit = {
        navController.navigate(Screens.Login().getRouteWithArgs(isEmailVerified = it)){
            launchSingleTop = true
            popUpTo(navController.graph.findStartDestination().id){
                inclusive = true
            }
        }
    }

    val navigateToHomeGraph: () -> Unit = {
        navController.navigate(Screens.NESTED_HOME_ROUTE) {
            launchSingleTop = true
            // Pop up to the start destination of the current graph (auth graph)
            popUpTo(Screens.NESTED_AUTH_ROUTE) {
                inclusive = true
            }
        }
    }
}

fun NavController.navigateToSingleTop(route:String){
    navigate(route){
        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}