package com.example.bibleapp.versecontent.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.bibleapp.ui.theme.Purple500
import com.example.bibleapp.ui.theme.blue
import kotlinx.coroutines.launch

val bottomNavHeight = 65.dp
@Composable
fun BottomBar(
    onClickPrevious:() -> Unit,
    onClickNext: () -> Unit
) {
    val backGround = Brush.linearGradient(listOf(Purple500, blue))

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .height(bottomNavHeight)
                .fillMaxWidth(),
            elevation = 32.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .myWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(backGround)
                        .clickable {
                            onClickPrevious()
                        }
                        .offset {
                            IntOffset.Zero
                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.myPadding()
                    )
                    Text(
                        text = "Previous",
                        color = Color.White,
                        modifier = Modifier.myPadding()
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .myWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(backGround)
                        .clickable {
                            onClickNext()
                        }
                        .offset {
                            IntOffset.Zero
                        }
                ) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        modifier = Modifier.myPadding()
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.myPadding()
                    )
                }

            }
        }
    }
}

fun Modifier.myPadding(): Modifier {
    return this.padding(
        horizontal = 12.dp,
        vertical = 8.dp
    )
}

fun Modifier.myWidth(): Modifier {
    return this.width(
        150.dp
    )
}

@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    isClicked: Boolean,
) {

    LaunchedEffect(key1 = isClicked) {
        val tween: TweenSpec<Int> = tween(250, easing = LinearOutSlowInEasing)
        if (isClicked) {
            launch {
                val animation = TargetBasedAnimation(
                    animationSpec = tween,
                    typeConverter = Int.VectorConverter,
                    initialValue = 0,
                    targetValue = 0
                )
                val startTime = withFrameNanos { it }
                do {
                    val playTime = withFrameNanos { it } - startTime
                } while (!animation.isFinishedFromNanos(playTime))
            }

            launch {
                val animation1 = TargetBasedAnimation(
                    animationSpec = tween,
                    typeConverter = Int.VectorConverter,
                    initialValue = 0,
                    targetValue = 0
                )
                val startTime1 = withFrameNanos { it }
                do {
                    val playTime = withFrameNanos { it } - startTime1
                } while (!animation1.isFinishedFromNanos(playTime))
            }
        }
    }
}

