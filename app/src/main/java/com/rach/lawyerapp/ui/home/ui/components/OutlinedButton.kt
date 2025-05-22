package com.rach.lawyerapp.ui.home.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.rach.lawyerapp.ui.theme.SemiboldAndInter

@Composable
fun CustomOutlinedButton(
    @StringRes label: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    shape: Shape = ButtonDefaults.outlinedShape,
    borderStroke: BorderStroke? = ButtonDefaults.outlinedButtonBorder
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = color,
        border = borderStroke
    ) {
        Text(
            text = stringResource(label),
            style = SemiboldAndInter
        )
    }
}