package com.rach.lawyerapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingViewHorizontal(
    isLoading:Boolean,
    modifier: Modifier = Modifier) {

    AnimatedVisibility(
        modifier = modifier,
        visible = isLoading,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            ) { }

        }
    }

}