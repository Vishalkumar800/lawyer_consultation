package com.rach.lawyerapp.navigation.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rach.lawyerapp.R
import com.rach.lawyerapp.navigation.Screens
import com.rach.lawyerapp.ui.home.ui.bars.UserProfile
import com.rach.lawyerapp.ui.home.ui.bars.WalletAndHistory

fun NavGraphBuilder.navDrawerGraph(navHostController: NavHostController) {
    navigation(
        startDestination = Screens.NavDrawer.Profile.route,
        route = Screens.NavDrawerRoute.route
    ) {
        composable(Screens.NavDrawer.Profile.route) {
            UserProfile(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding))
            )
        }

        composable(Screens.NavDrawer.Wallet.route) {
            WalletAndHistory(
                balance = 90,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding))
            )
        }
    }
}