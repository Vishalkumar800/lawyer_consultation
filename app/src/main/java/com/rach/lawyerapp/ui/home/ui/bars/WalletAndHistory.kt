package com.rach.lawyerapp.ui.home.ui.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.home.data.dataSource.TransactionDataSource
import com.rach.lawyerapp.ui.home.data.model.TransactionModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.boldRaiText

@Composable
fun WalletAndHistory(
    balance: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {

            Text(
                text = stringResource(R.string.available_balance),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.normal_padding))
            )
        }

        //Balance
        item {

            Text(
                text = "\u20B9  $balance",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 25.sp)
            )
        }

        item {  //Transaction History Text
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_space)))
            TransactionHistoryText()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_space)))
        }


        items(TransactionDataSource.transactionData) { data ->
            TransactionHistoryDesign(
                transactionModel = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.normal_padding),
                        start = dimensionResource(R.dimen.small_space)
                    )
            )
        }

    }
}


@Composable
private fun TransactionHistoryText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.transaction_history),
        style = boldRaiText
    )
}

@Composable
private fun TransactionHistoryDesign(
    transactionModel: TransactionModel, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(60.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    transactionModel.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 24.sp)
                )
            }
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.large_space)))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(transactionModel.addedOrDeduct)
            Text(transactionModel.date)
        }

        // added + and - sign
        val amountWithSign = when (transactionModel.addedOrDeduct.toLowerCase()) {
            "added" -> "+${transactionModel.amount}"
            "deducted" -> "-${transactionModel.amount}"
            else -> {
                "${transactionModel.amount}"
            }
        }

        Text(
            text = amountWithSign,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

    }
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        WalletAndHistory(
            balance = 90,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}
