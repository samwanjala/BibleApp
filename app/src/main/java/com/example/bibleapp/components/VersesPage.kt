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
import com.example.bibleapp.domain.model.Verse
import com.example.bibleapp.viewmodel.MainViewModel

@Composable
fun VersesPage(
    viewModel: MainViewModel,
    onClickItem: (bibleId: String, verseId: String, verse: String) -> Unit
) {
    val verses = viewModel.verses.collectAsState().value

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        VersesGrid(
            verses = verses,
            onClickItem = { bibleId, verseId, verse ->
                onClickItem(bibleId, verseId, verse)
            }
        )
    }

    UIState(viewModel = viewModel)

    LaunchedEffect(key1 = viewModel.isConnected) {
        viewModel.getVerses(chapterId = viewModel.chapterIdForRequestedVerse)
    }
}

@Composable
fun VersesGrid(
    verses: List<Verse>,
    onClickItem: (bibleId: String, verseId: String, verse: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(bottom = bottomNavHeight)
    ) {
        items(verses) { verse ->
            GridItem(
                name = verse.reference,
                onClickItem = { onClickItem(verse.bibleId, verse.id, verse.reference) }
            )
        }
    }
}
