package com.rach.lawyerapp.ui.home.ui.bars

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rach.lawyerapp.navigation.appBottomAppBarList

@Composable
fun BottomAppBar(
    currentRoute:String?,
    navHostController: NavHostController,
    modifier: Modifier = Modifier) {

    NavigationBar(
        modifier = modifier
    ) {
        appBottomAppBarList.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.dRoute,
                onClick = {
                    navHostController.navigate(item.dRoute){
                        popUpTo(navHostController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                },
                label = {
                    Text(text = item.dTitle)
                },
            )
        }
    }

}