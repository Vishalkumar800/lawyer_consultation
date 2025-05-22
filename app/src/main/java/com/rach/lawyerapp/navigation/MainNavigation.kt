package com.rach.lawyerapp.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rach.lawyerapp.navigation.graph.authNavigation
import com.rach.lawyerapp.navigation.graph.homeNavigation
import com.rach.lawyerapp.navigation.graph.navDrawerGraph
import com.rach.lawyerapp.navigation.ui.CustomTopAppBar
import com.rach.lawyerapp.ui.home.ui.bars.AppNavDrawer
import com.rach.lawyerapp.ui.home.ui.bars.BottomAppBar
import com.rach.lawyerapp.ui.home.viewModel.LawyerUiEvents
import com.rach.lawyerapp.ui.home.viewModel.LawyerViewModel
import com.rach.lawyerapp.ui.login.viewModel.LoginViewModel
import com.rach.lawyerapp.ui.wallet.viewModel.RazorPayViewModel
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(
    navHostController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    lawyerViewModel: LawyerViewModel = hiltViewModel(),
    razorPayViewModel: RazorPayViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val scope = rememberCoroutineScope()
    // drawer width
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val drawerWidth = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        with(density) { (configuration.screenWidthDp * 0.4).dp }
    } else {
        280.dp
    }

    // isWe on Home Screen
    val isHomeScreen = currentRoute == Screens.BottomScreen.Home.route
    val currentScreen = Screens.fromRoute(currentRoute)

    val uiState by lawyerViewModel.uiState.collectAsState()

    val authState by remember { mutableStateOf(loginViewModel.hasUserVerified()) }
    val startRoute = if (authState) Screens.NESTED_HOME_ROUTE else Screens.NESTED_AUTH_ROUTE

    val navAction = remember { MyAppNavigation(navHostController) }


    val authRoutes = authScreensNotVisibleInHomeScreens.mapNotNull {
        when (it) {
            is Screens.AuthenticationScreen -> it.authRoute
            is Screens.Login -> it.routeWithArgs
            else -> null
        }
    }


    val bottomAppBarRoute = appBottomAppBarList.map { it.dRoute }

    if (currentRoute != null && currentRoute !in authRoutes) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppNavDrawer(
                    currentRoute = currentRoute,
                    drawerState = drawerState,
                    navHostController = navHostController,
                    modifier = Modifier
                        .widthIn(max = drawerWidth)
                        .fillMaxSize()
                )
            },
            modifier = Modifier
        ) {
            Scaffold(
                topBar = {
                    CustomTopAppBar(
                        title = currentScreen.title,
                        canNavigateBack = currentRoute != Screens.BottomScreen.Home.route,
                        navigateUp = { navHostController.navigateUp() },
                        openDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        onAddMoneyClicked = {
                            scope.launch {
                                navHostController.navigate(Screens.HomeItem.AddMoney.route)
                            }
                        },
                        onMessageClicked = {},
                        modifier = Modifier.fillMaxWidth(),
                        isHomeScreen = isHomeScreen,
                        lawyerViewModel = lawyerViewModel,
                        searchText = uiState.searchText,
                        onSearchTextChange = {
                            lawyerViewModel.onEvents(events = LawyerUiEvents.OnSearchTextChange(it))
                        }
                    )
                },
                bottomBar = {
                    if (currentRoute in bottomAppBarRoute) {
                        BottomAppBar(
                            currentRoute = currentRoute,
                            navHostController = navHostController,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            ) { paddingValues ->

                NavHostContent(
                    navHostController = navHostController,
                    paddingValues = paddingValues,
                    startDestination = startRoute,
                    navAction = navAction,
                    razorPayViewModel = razorPayViewModel
                )
            }
        }
    } else {
        Scaffold { paddingValues ->
            NavHostContent(
                navHostController = navHostController,
                paddingValues = paddingValues,
                startDestination = startRoute,
                navAction = navAction,
                razorPayViewModel = razorPayViewModel
            )
        }
    }
}

@Composable
fun NavHostContent(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    navAction: MyAppNavigation,
    startDestination: String,
    viewModel: LawyerViewModel = hiltViewModel(),
    razorPayViewModel: RazorPayViewModel = hiltViewModel()
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        authNavigation(navAction = navAction, navHostController = navHostController)
        homeNavigation(navHostController = navHostController, viewModel = viewModel, razorPayViewModel = razorPayViewModel )
        navDrawerGraph(navHostController = navHostController)

    }
}