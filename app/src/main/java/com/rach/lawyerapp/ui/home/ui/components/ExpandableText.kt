package com.rach.lawyerapp.ui.home.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.theme.interFontFamily
import com.rach.lawyerapp.ui.theme.poppinsFontFamily


@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    animationDuration: Int = 300,
    collapsedMaxLines: Int = 4,
    wordLimit: Int = 50
) {
    var isExpanded by remember { mutableStateOf(false) }
    val words = text.split(" ")
    val isTextLong = words.size > wordLimit

    val truncatedText =
        if (isTextLong && !isExpanded) words.take(wordLimit).joinToString(" ") + "..." else text

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (isTextLong) {
                    isExpanded = !isExpanded
                }
            }
    ) {
        Text(
            text = truncatedText,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.animateContentSize(
                animationSpec = tween(durationMillis = animationDuration)
            )
        )

        AnimatedVisibility(
            visible = isTextLong,
            enter = fadeIn(animationSpec = tween(durationMillis = animationDuration)),
            exit = fadeOut(animationSpec = tween(durationMillis = animationDuration))
        ) {
            Text(
                text = if (isExpanded) stringResource(R.string.see_less) else stringResource(R.string.see_more), // Fix: Use isExpanded
                color = MaterialTheme.colorScheme.primary,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .alpha(if (isExpanded) 0.8f else 1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableTextPreview() {
    val longText = "sbhahdsh sdkjnb cbjhdf dfsmhbdn efwb,bv f,njefujhbn cdcbj ncjfemwn cbnc cnmdsc csmnc dcbdc dcjv ncvvj ,dmndcd kjfewbmc xmnc cvn dmcdm  cvdvb.nifn svmer,dfscjxm nf,bdjbmn dc nmd  dn n ndv ndsnbeh,jfnbfbj,frmfr r"
    ExpandableText(
        text = longText,
        modifier = Modifier.padding(16.dp)
    )
}