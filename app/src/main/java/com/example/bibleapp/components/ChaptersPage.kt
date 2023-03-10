package com.example.bibleapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bibleapp.domain.model.Chapter
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

    UIState(viewModel = viewModel)

    LaunchedEffect(key1 = viewModel.isConnected) {
        viewModel.getChapters(bookId = viewModel.bookIdForRequestedChapter)
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