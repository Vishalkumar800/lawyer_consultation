package com.rach.lawyerapp.navigation.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rach.lawyerapp.R
import com.rach.lawyerapp.navigation.Screens
import com.rach.lawyerapp.ui.home.ui.HomeScreen
import com.rach.lawyerapp.ui.home.ui.LawyerHistoryMainUi
import com.rach.lawyerapp.ui.home.viewModel.LawyerViewModel
import com.rach.lawyerapp.ui.wallet.viewModel.RazorPayViewModel
import com.rach.lawyerapp.ui.wallet.walletBalance.ui.AddMoneyScreens


fun NavGraphBuilder.homeNavigation(
    navHostController: NavHostController,
    viewModel: LawyerViewModel,
    razorPayViewModel: RazorPayViewModel
) {
    navigation(
        startDestination = Screens.BottomScreen.Home.route,
        route = Screens.NESTED_HOME_ROUTE
    ) {

        //HomeScreen
        composable(Screens.BottomScreen.Home.route) {
            HomeScreen(
                onDetailsClick = { lawyer ->
                    navHostController.navigate(
                        Screens.HomeItem.LawyerHistoryMainUi.createRoute(
                            lawyer.id
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding)),
                viewModel = viewModel
            )
        }

        // Add Money
        composable(Screens.HomeItem.AddMoney.route) {
            AddMoneyScreens(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        dimensionResource(R.dimen.normal_padding)
                    ),
                viewModel = razorPayViewModel
            )
        }

        // Lawyer History Ui

        composable(
            Screens.HomeItem.LawyerHistoryMainUi.route,
            arguments = listOf(navArgument("lawyerId") {
                type = NavType.StringType
                defaultValue = "unknown"
            })
        ) { navBackStackEntry ->
            val lawyerId = navBackStackEntry.arguments?.getString("lawyerId")
            val uiState by viewModel.uiState.collectAsState()
            val selectedLawyers = uiState.lawyers.find { it.id == lawyerId }
                ?: uiState.lawyers.find { it.id == null && lawyerId == "unknown" }

            LawyerHistoryMainUi(
                lawyerModel = selectedLawyers,
                onBackClick = { navHostController.popBackStack() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding))
            )
        }

        // Admin Screen

    }
}