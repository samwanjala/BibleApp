package com.example.bibleapp.book.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bibleapp.book.ui.viewmodel.BookViewModel
import com.example.bibleapp.components.GridItem
import com.example.bibleapp.domain.model.Book

@Composable
fun BooksPage(
    viewModel: BookViewModel,
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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