package com.rach.lawyerapp.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.components.CustomOutLinedTextField
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.theme.kufamFontFamily
import com.rach.lawyerapp.ui.theme.poppinsFontFamily

@Composable
private fun LawyerRegistrationUi(modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    var enterName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {

        Title(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium)))

        //Enter Name
        CustomOutLinedTextField(
            label = R.string.name,
            value = enterName,
            onValueChange = {
                enterName = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        // Enter Phone Number
        CustomOutLinedTextField(
            label = R.string.enter_phone_number,
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        // Enter email
        CustomOutLinedTextField(
            label = R.string.enter_email,
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        //Description
        CustomOutLinedTextField(
            label = R.string.lawyer_description,
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 400.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        //State
        CustomOutLinedTextField(
            label = R.string.state,
            value = state,
            onValueChange = {
                state = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        //District
        CustomOutLinedTextField(
            label = R.string.district,
            value = district,
            onValueChange = {
                district = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))

        //city
        CustomOutLinedTextField(
            label = R.string.city,
            value = city,
            onValueChange = {
                city = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.outlined_space)))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Submit", fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium
            )
        }
    }

}

@Composable
private fun Title(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please Fill Registration Form",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Our Team will contact in 24 hours",
            fontFamily = kufamFontFamily
        )
    }

}

@AppPreview
@Composable
private fun Preview() {
    LawyerAppTheme {
        LawyerRegistrationUi(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}