package com.rach.lawyerapp.ui.home.ui.components

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.greenColor
import com.rach.lawyerapp.ui.theme.poppinsFontFamily
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import timber.log.Timber


@Composable
fun LawyerCard(
    darkTheme: Boolean = isSystemInDarkTheme(),
    onDetailsClick: () -> Unit,
    lawyerModel: LawyerModel,
    userId:String,
    userName:String,
    modifier: Modifier = Modifier
) {
    val bgcolor =
        if (darkTheme) MaterialTheme.colorScheme.background.copy(alpha = 0.3f) else Color.White
    OutlinedCard(
        onClick = { onDetailsClick() }, // What happen on the click of this item
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.small_padding)
        ),
        colors = CardDefaults.cardColors(bgcolor)
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(lawyerModel.image)
                    .crossfade(true)
                    .build(),
                contentDescription = lawyerModel.name,
                modifier = Modifier
                    .size(110.dp)
                    .border(width = 1.dp, color = Color.Yellow, shape = CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.testing_avatar),
                placeholder = painterResource(R.drawable.loading)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.normal_padding)))
            //Name Exp Language Qualification
            LawyerProfileDesign(
                lawyerModel = lawyerModel,
                modifier = Modifier.weight(1f)
            )

            Column(
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_verified_24),
                    contentDescription = stringResource(R.string.verified),
                    tint = greenColor
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))

                VideoOrVoiceCallButton(isVideoCallBoolean = false) { button ->
                    if (userId.isNotEmpty()) {
                        button.setInvitees(mutableListOf(ZegoUIKitUser(userId, userName)))
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))
                VideoOrVoiceCallButton(isVideoCallBoolean = true) {button ->
                    if (userId.isNotEmpty()) {
                        button.setInvitees(mutableListOf(ZegoUIKitUser(userId, userName)))
                    }
                }
            }

        }


    }
}

@Composable
private fun LawyerProfileDesign(
    lawyerModel: LawyerModel,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_space))
    ) {
        Text(
            text = lawyerModel.name,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = lawyerModel.education,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = lawyerModel.language,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )

        Text("Exp - ${lawyerModel.experience} years", style = MaterialTheme.typography.labelMedium)
        Text("\u20B9 ${lawyerModel.perMinPrice}/min", style = MaterialTheme.typography.labelMedium)

    }

}

@Composable
fun ChatAndCall(
    modifier: Modifier = Modifier,
    isBusy: Boolean = true,
    callClicked: () -> Unit,
    chatClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium))
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_verified_24),
            contentDescription = stringResource(R.string.verified),
            tint = greenColor
        )
        //Call
        ChatAndCallButton(
            onClick = callClicked,
            label = if (isBusy) R.string.busy else R.string.call,
            modifier = Modifier,
            color = if (isBusy) Color.Red else greenColor
        )
        ChatAndCallButton(
            onClick = { chatClicked() },
            label = R.string.chat,
            modifier = Modifier,
            color = greenColor
        )
    }
}

@Composable
fun ChatAndCallButton(
    onClick: () -> Unit,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    color: Color
) {
    OutlinedButton(
        modifier = modifier.size(height = 40.dp, width = 90.dp),
        onClick = { onClick() },
        border = BorderStroke(
            width = 1.dp,
            color = color
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = stringResource(label),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
            color = color
        )
    }
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        LawyerCard(
            onDetailsClick = {},
            userId = "000",
            userName = "999",
            modifier = Modifier.fillMaxWidth(),
            lawyerModel = LawyerModel(
                name = "John Doe",
                education = "BA LLB",
                language = "English, Spanish",
                experience = 10,
                perMinPrice = 5
            )
        )
    }
}