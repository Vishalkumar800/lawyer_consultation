package com.rach.lawyerapp.ui.home.ui.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.theme.LawyerAppTheme

@Composable
fun UserProfile(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //userImage
        item {
            UserProfileImage(
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.large_space))
            )

            ProfileEdit(
                label = "Name",
                title = "Vishal",
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun UserProfileImage(modifier: Modifier = Modifier) {


    Image(
        painter = painterResource(R.drawable.testing_avatar),
        modifier = modifier
            .size(110.dp)
            .border(width = 1.dp, color = Color.Yellow, shape = CircleShape),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )


}


@Composable
private fun ProfileEdit(
    label: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
        )

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = title)
        }
    }
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        UserProfile(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}