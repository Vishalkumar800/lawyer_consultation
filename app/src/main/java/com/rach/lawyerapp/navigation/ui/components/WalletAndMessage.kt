package com.rach.lawyerapp.navigation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.wallet.viewModel.RazorPayViewModel

@Composable
fun WalletAndMessage(
    onWalletClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMessageClick: () -> Unit,
    isSearchBarVisible: Boolean,
    modifier: Modifier = Modifier,
    viewModel: RazorPayViewModel = hiltViewModel()
) {

    val state by viewModel.walletUiState.collectAsState()

    if (!isSearchBarVisible) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Wallet
            WalletCardTopAppBar(
                onWalletClick = onWalletClick,
                balance = state.balance
            )

            //Search
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = Color.White
                )
            }

            //Message
            IconButton(onClick = onMessageClick) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = stringResource(R.string.message),
                    tint = Color.White
                )
            }

        }
    }

}

@Composable
private fun WalletCardTopAppBar(
    onWalletClick: () -> Unit,
    modifier: Modifier = Modifier,
    balance: Int
) {
    Surface(
        modifier = modifier
            .clickable { onWalletClick() },
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_space))
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalanceWallet,
                contentDescription = "Account Balance Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(14.dp)
            )

            Text(
                "\u20B9 $balance ",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                imageVector = Icons.Filled.AddCircle,
                tint = Color.Blue,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        WalletAndMessage({}, {}, {}, isSearchBarVisible = false
        )
    }
}