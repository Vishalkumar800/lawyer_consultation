package com.rach.lawyerapp.ui.home.ui.bars

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.rach.lawyerapp.R
import com.rach.lawyerapp.navigation.appNavDrawerList
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navHostController: NavHostController
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val drawerBackgroundColor = MaterialTheme.colorScheme.surface

    LazyColumn(
        modifier = modifier
            .background(drawerBackgroundColor)
            .padding(vertical = dimensionResource(R.dimen.normal_padding))
    ) {

        //Title
        item {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_space)))
            Text(
                text = stringResource(R.string.app_name), modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.outlined_space),
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.outlined_space)))
        }

        //Content
        item {
            appNavDrawerList.forEach { item ->
                NavigationDrawerItem(
                    selected = currentRoute == item.navRoute,
                    label = { Text(text = item.navTitle) },
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        when (item.navRoute) {
                            "profile" -> navHostController.navigate(item.navRoute)
                            "walletHistory" -> navHostController.navigate(item.navRoute)
                            "privacyPolicy" -> navHostController.navigate(item.navRoute)
                            "share" -> {
                                scope.launch {
                                    drawerState.close()
                                }

                                val appLink =
                                    "https://play.google.com/store/apps/details?id=com.rach.co"
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "plain/text"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check Out This Amazing app! $appLink"
                                    )

                                }

                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share via"
                                    )
                                )
                            }

                            else -> {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.navTitle
                        )
                    }, modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.medium),
                        )

                )
            }
        }

    }

}