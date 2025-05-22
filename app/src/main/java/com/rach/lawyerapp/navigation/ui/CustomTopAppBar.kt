package com.rach.lawyerapp.navigation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.navigation.ui.components.WalletAndMessage
import com.rach.lawyerapp.ui.home.viewModel.LawyerUiEvents
import com.rach.lawyerapp.ui.home.viewModel.LawyerViewModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    onAddMoneyClicked: () -> Unit,
    onMessageClicked: () -> Unit,
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHomeScreen: Boolean = false,
    lawyerViewModel: LawyerViewModel
) {
    val topAppBarColors = if (darkTheme) MaterialTheme.colorScheme.background else primaryLight

    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topAppBarColors,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        title = {
            if (isSearchBarVisible && isHomeScreen) {
                TextField(
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it) // ✅ Fix: Assigning properly
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(text = stringResource(R.string.search))
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onSearchTextChange("") // ✅ Fix: Clearing search text properly
                                isSearchBarVisible = false
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            } else {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.normal_padding)),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                )
            }
        },
        navigationIcon = {
            if (isSearchBarVisible) {
                IconButton(
                    onClick = {
                        onSearchTextChange("") // ✅ Fix: Clearing text when closing search
                        isSearchBarVisible = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.arrow_back)
                    )
                }
            } else if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.arrow_back)
                    )
                }
            } else {
                IconButton(onClick = openDrawer) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = stringResource(R.string.menu)
                    )
                }
            }
        },
        actions = {
            if (isHomeScreen) {
                WalletAndMessage(
                    onWalletClick = { onAddMoneyClicked() },
                    onSearchClick = { isSearchBarVisible = true },
                    onMessageClick = { onMessageClicked() },
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.normal_padding)),
                    isSearchBarVisible = isSearchBarVisible
                )
            }
        }
    )

    LaunchedEffect(isSearchBarVisible) {
        if (isSearchBarVisible) {
            focusRequester.requestFocus()
        }
    }
}


