package com.rach.lawyerapp.ui.wallet.walletBalance.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.MainActivity
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.LoadingView
import com.rach.lawyerapp.ui.home.data.dataSource.DisCountDataSource
import com.rach.lawyerapp.ui.home.data.model.DiscountModel
import com.rach.lawyerapp.ui.home.ui.components.CustomOutlinedButton
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.poppinsFontFamily
import com.rach.lawyerapp.ui.wallet.viewModel.RazorPayViewModel
import com.rach.lawyerapp.ui.wallet.viewModel.WalletUiEvents

@Composable
fun AddMoneyScreens(
    modifier: Modifier = Modifier,
    viewModel: RazorPayViewModel = hiltViewModel()
) {
    val state by viewModel.walletUiState.collectAsState()

    val activity = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(R.string.add_money),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))
            EnterAmount(
                value = state.userEnterAmount,
                onValueChange = {
                    viewModel.onEvents(events = WalletUiEvents.OnUserEnterAmount(it))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_space)))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(dimensionResource((R.dimen.outlined_space))),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium))
            ) {
                items(DisCountDataSource.discountData) { data ->
                    DiscountCard(
                        discountModel = data,
                        onClick = {
                            viewModel.onEvents(
                                events = WalletUiEvents.OnUserEnterAmount(
                                    data.value.toString()
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

        Column {
            //RazorPay Button
            CustomOutlinedButton(
                label = R.string.add_money,
                onClick = {
                    viewModel.startPayment(activity as MainActivity)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    }

    if (state.isLoading) {
        LoadingView()
    }

}

@Composable
private fun EnterAmount(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier.onFocusChanged {
            focused = it.isFocused
        },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding))
            ) {
                Text(
                    text = "\u20B9",
                    style = MaterialTheme.typography.displayMedium
                )

                if (value.isEmpty() && !focused) {
                    Text("0", style = MaterialTheme.typography.titleMedium)
                }
                innerTextField()
            }
        }
    )
}


@Composable
private fun DiscountCard(
    discountModel: DiscountModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedCard(
        modifier = modifier.clickable { onClick() },
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.normal_padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium))
        ) {
            Text(
                text = "${discountModel.value}",
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 20.sp),
            )

            Text(
                "${discountModel.discount}% Extra",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                ),
            )

        }
    }

}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        AddMoneyScreens(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}