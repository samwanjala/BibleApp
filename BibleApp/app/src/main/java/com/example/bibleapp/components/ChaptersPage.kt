package com.example.bibleapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bibleapp.data.Chapter
import com.example.bibleapp.viewmodel.MainViewModel

@Composable
fun ChaptersPage(
    viewModel: MainViewModel,
    onClickItem: (bibleId: String, chapterId: String, chapter: String) -> Unit
) {
    val chapters = viewModel.chapters.collectAsState().value

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ChaptersGrid(
            chapters = chapters,
            onClickItem = { bibleId, chapterId, chapter ->
                onClickItem(bibleId, chapterId, chapter)
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(
            viewModel.isLoading
        ) {
            CircularProgressIndicator()
        }

        if (
            !viewModel.isConnected
        ){
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.WifiOff,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "No internet connection")
            }
        }

        if (
            viewModel.isError && viewModel.isConnected
        ){
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "Something went wrong")
            }
        }
    }

    LaunchedEffect(key1 = viewModel.isConnected){
        if (viewModel.isConnected && chapters.isEmpty()){
            viewModel.getChapters(bookId = viewModel.bookIdForRequestedChapter)
        }
    }
}

@Composable
fun ChaptersGrid(
    chapters: List<Chapter>,
    onClickItem: (bibleId: String, chapterId: String, chapter: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(bottom = bottomNavHeight)
    ) {
        items(chapters) { chapter ->
            GridItem(
                name = chapter.number,
                onClickItem = {
                    onClickItem(chapter.bibleId, chapter.id, chapter.number)
                }
            )
        }
    }
}