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
import com.example.bibleapp.data.Book
import com.example.bibleapp.viewmodel.MainViewModel

@Composable
fun BooksPage(
    viewModel: MainViewModel,
    onClickItem: (bibleId: String, bookId: String, bookName: String) -> Unit
) {
    val books = viewModel.books.collectAsState().value

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        BooksGrid(
            books = books,
            onClickItem = { bibleId, bookId, bookName ->
                onClickItem(bibleId, bookId, bookName)
            }
        )
    }

    UIState(viewModel = viewModel)

    LaunchedEffect(key1 = viewModel.isConnected) {
        viewModel.getBooks()
    }
}

@Composable
fun BooksGrid(
    books: List<Book>,
    onClickItem: (bibleId: String, bookId: String, bookName: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(bottom = bottomNavHeight)
    ) {
        items(books) { book ->
            GridItem(
                name = book.name,
                onClickItem = {
                    onClickItem(book.bibleId, book.id, book.name)
                }
            )
        }
    }
}