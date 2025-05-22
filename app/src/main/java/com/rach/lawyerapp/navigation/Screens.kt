package com.rach.lawyerapp.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.rach.lawyerapp.R


sealed class Screens(val title: String, val route: String) {

    companion object {
        const val NESTED_AUTH_ROUTE = "auth_route"
        const val NESTED_HOME_ROUTE = "home_route"
        const val IS_EMAIL_SENT_ARGS = "isEmailSent"

        fun fromRoute(route: String?): Screens {
            return when (route) {
                BottomScreen.Home.route -> BottomScreen.Home
                BottomScreen.Chats.route -> BottomScreen.Chats
                BottomScreen.Call.route -> BottomScreen.Call
                BottomScreen.Admin.route -> BottomScreen.Admin
                NavDrawer.Profile.route -> NavDrawer.Profile
                NavDrawer.Wallet.route -> NavDrawer.Wallet
                HomeItem.AddMoney.route -> HomeItem.AddMoney
                HomeItem.LawyerHistoryMainUi.route -> HomeItem.LawyerHistoryMainUi
                else -> NoRoute // Fallback to Auth if route is unknown
            }
        }
    }

    object NoRoute:Screens("NoRoute","no_route")

    object NavDrawerRoute : Screens("NavdrawerRoute", "navDrawerRoute")


    //Authentication Screen
    sealed class AuthenticationScreen(val authTitle: String, val authRoute: String) :
        Screens(authTitle, authRoute) {
        object ForgotPassword :
            AuthenticationScreen(authTitle = "Forgot Password", authRoute = "forgot_password")

        object ResetPassword :
            AuthenticationScreen(authTitle = "Reset Password", authRoute = "reset_password")
    }

    data class Login(
        val routes: String = "login",
        val routeWithArgs: String = "$routes/{$IS_EMAIL_SENT_ARGS}"
    ) :
        Screens("Login", route = routes) {

            fun getRouteWithArgs(isEmailVerified:Boolean):String{
                return "$routes/$isEmailVerified"
            }

    }

    // Bottom App Bar
    sealed class BottomScreen(val dTitle: String, val dRoute: String, val icon: ImageVector) :
        Screens(dTitle, dRoute) {
        object Home : BottomScreen(dTitle = "Home", dRoute = "home", icon = Icons.Outlined.Home)
        object Chats :
            BottomScreen(dTitle = "Chats", "chats", icon = Icons.AutoMirrored.Outlined.Message)

        object Call : BottomScreen(dTitle = "Call", dRoute = "call", icon = Icons.Outlined.Call)
        object Admin : BottomScreen(
            dTitle = "Admin",
            dRoute = "admin",
            icon = Icons.Outlined.AdminPanelSettings
        )
    }

    // Nav Drawer
    sealed class NavDrawer(val navTitle: String, val navRoute: String, @DrawableRes val icon: Int) :
        Screens(navTitle, navRoute) {

        object Profile : NavDrawer(
            navTitle = "Profile",
            navRoute = "profile",
            icon = R.drawable.filled_profile_icon
        )

        object Wallet : NavDrawer(
            navTitle = "Wallet History",
            navRoute = "walletHistory",
            icon = R.drawable.baseline_account_balance_wallet_24
        )

        object PrivacyPolicy : NavDrawer(
            navTitle = "Privacy Policy",
            navRoute = "privacyPolicy",
            icon = R.drawable.privacy_policy_icon
        )

        object Share :
            NavDrawer(navTitle = "Share", navRoute = "share", icon = R.drawable.baseline_share_24)
    }

    //HomeScreens Item
    sealed class HomeItem(val homeTitle: String, val homeRoute: String) :
        Screens(homeTitle, homeRoute) {
        object AddMoney : HomeItem(homeTitle = "AddMoney", homeRoute = "addMoney")
        object LawyerHistoryMainUi : HomeItem(homeTitle = "Lawyer History", homeRoute = "lawyerHistory/{lawyerId}"){
            fun createRoute(lawyerId:String?) = "lawyerHistory/${lawyerId ?:"unknown"}"
        }
    }


}

val authScreensNotVisibleInHomeScreens = listOf(
    Screens.Login(),
    Screens.AuthenticationScreen.ForgotPassword,
    Screens.AuthenticationScreen.ResetPassword
)

val appBottomAppBarList = listOf(
    Screens.BottomScreen.Home,
    Screens.BottomScreen.Chats,
    Screens.BottomScreen.Call,
    Screens.BottomScreen.Admin
)

val appNavDrawerList = listOf(
    Screens.NavDrawer.Profile,
    Screens.NavDrawer.Wallet,
    Screens.NavDrawer.PrivacyPolicy,
    Screens.NavDrawer.Share
)