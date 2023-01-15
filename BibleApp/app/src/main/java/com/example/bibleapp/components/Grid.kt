package com.example.bibleapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bibleapp.ui.theme.Purple500
import com.example.bibleapp.ui.theme.blue

@Composable
fun GridItem(
    name: String,
    height: Dp = 80.dp,
    onClickItem: () -> Unit
) {
    val backGround = Brush.linearGradient(listOf(Purple500, blue))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(backGround)
            .clickable {
                onClickItem()
            }
    ) {
        Text(
            text = name,
            color = Color.White.copy(0.7f)
        )
    }
}