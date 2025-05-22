package com.rach.lawyerapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutLinedTextField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean = false,
    maxLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    placeHolder: String? = null,
    leadingIcon: Any? = null,
    trailingIcon: Any? = null
) {


    OutlinedTextField(
        label = { Text(text = stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        isError = error,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = shape,
        colors = colors,
        placeholder = {
            placeHolder?.let { Text(text = it) }
        },
        leadingIcon = if (leadingIcon != null) {
            { GetIcon(leadingIcon) }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            { GetIcon(trailingIcon) }
        } else null
    )

}

@Composable
private fun GetIcon(icon: Any) {
    when (icon) {
        is ImageVector -> Icon(imageVector = icon, contentDescription = null)
        is Int -> Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}