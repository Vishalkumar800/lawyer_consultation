package com.rach.lawyerapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.rach.lawyerapp.R

@Composable
fun CustomPasswordTextField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String,
    hasError:Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions
) {

    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = hasError,
        visualTransformation = if (showPassword.value){
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = {showPassword.value = !showPassword.value}
            ) {
                Icon(
                    painter = painterResource(if (showPassword.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                    contentDescription = null
                )
            }
        },
        maxLines = 1
    )

}