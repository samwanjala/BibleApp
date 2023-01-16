package com.example.bibleapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bibleapp.data.VerseContent
import com.example.bibleapp.viewmodel.MainViewModel

@Composable
fun VerseContentPage(
    verseContent: VerseContent,
    onClickPrevious: () -> Unit,
    viewModel: MainViewModel,
    onClickNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        verseContent.content?.let { Text(text = it) }
    }

    UIState(viewModel = viewModel)

    LaunchedEffect(key1 = viewModel.isConnected) {
        viewModel.getVerseContent(verseId = viewModel.verseIdForRequestedVerseContent)
    }

    BottomBar(
        onClickNext = {
            onClickNext()
        },
        onClickPrevious = {
            onClickPrevious()
        }
    )
}