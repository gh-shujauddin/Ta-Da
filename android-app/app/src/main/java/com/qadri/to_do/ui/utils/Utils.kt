package com.qadri.to_do.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.app.FrameMetricsAggregator.ANIMATION_DURATION

@Composable
fun MutableState<Boolean>.ExpandAndShrinkAnimation(
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = this.value,
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION
            )
        )
    ) {
        content()
    }
}