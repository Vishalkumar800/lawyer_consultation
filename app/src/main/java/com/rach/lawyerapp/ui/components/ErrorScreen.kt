package com.rach.lawyerapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.boldRaiText
import com.rach.lawyerapp.ui.theme.buttonText

@Composable
fun ErrorScreen(
    retry: () -> Unit,
    @StringRes errorMessage:Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.cloud_off),
            modifier = Modifier.size(60.dp),
            contentDescription = ""
        )

        Text(
            text = stringResource(errorMessage),
            modifier = Modifier.padding(dimensionResource(R.dimen.normal_padding)),
            style = boldRaiText
        )
        Button(
            onClick = retry
        ) {
            Text(
                text = stringResource(R.string.retry),
                style = buttonText
            )
        }

    }
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        ErrorScreen(
            retry = {},
            errorMessage = R.string.failed_to_load,modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}