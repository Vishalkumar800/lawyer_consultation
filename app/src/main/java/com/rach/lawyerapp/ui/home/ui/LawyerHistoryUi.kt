package com.rach.lawyerapp.ui.home.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.ErrorScreen
import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.ui.components.ChatAndCallButton
import com.rach.lawyerapp.ui.home.ui.components.ExpandableText
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.greenColor
import com.rach.lawyerapp.ui.theme.interJiSemiBold


@Composable
fun LawyerHistoryMainUi(
    lawyerModel: LawyerModel?,
    modifier: Modifier = Modifier,
    onBackClick:() -> Unit
) {
    when{
        lawyerModel == null -> {
            ErrorScreen(
                modifier = modifier,
                retry = onBackClick,
                errorMessage = R.string.lawyer_data_not_found
            )
        }
        else -> {
            LawyerHistoryUi(
                lawyerModel = lawyerModel,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LawyerHistoryUi(
    modifier: Modifier = Modifier,
    lawyerModel: LawyerModel,
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        LawyerImageCard(
            image = lawyerModel.image,
            contentDescription = lawyerModel.name,
            context = context,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        IntroAndCall(
            name = lawyerModel.name,
            lastName = lawyerModel.lastName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.outlined_space))
        )

        Text(
            text = "Description",
            style = interJiSemiBold
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium)))

        ExpandableText(
            text = lawyerModel.description
        )


    }

}

@Composable
private fun LawyerImageCard(
    image: String? = null,
    contentDescription: String,
    context: Context,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(image)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.testing_avatar),
            placeholder = painterResource(R.drawable.loading)
        )
    }
}

@Composable
private fun IntroAndCall(
    name: String,
    lastName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Name And Last Name
        Column {
            Row {
                Text(
                    text = name,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.medium))
                )
                Icon(
                    imageVector = Icons.Filled.Verified,
                    tint = greenColor,
                    contentDescription = null
                )
            }

            Text(lastName)
        }
        // Chat and Call Options
        Column {

            ChatAndCallButton(
                onClick = {},
                label = R.string.chat,
                color = greenColor
            )
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    LawyerAppTheme {
        LawyerHistoryUi(
            lawyerModel = LawyerModel(
                image = "dmd",
                name = "Vishal",
                lastName = "ki", education = "LLB", description = "My Name is Vishal",
                language = "Hindi , English",
                experience = 8, perMinPrice = 8, category = emptyList()
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}