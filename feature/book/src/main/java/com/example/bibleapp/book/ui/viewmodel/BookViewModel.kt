package com.example.bibleapp.book.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibleapp.domain.model.Book
import com.example.bibleapp.domain.repository.LocalRepository
import com.example.bibleapp.domain.repository.RemoteRepository
import com.example.bibleapp.util.ConnectivityStateObserver
import com.example.bibleapp.util.ConnectivityStateObserverImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val application: Application
) : ViewModel() {
    private val _books = MutableStateFlow(emptyList<Book>())
    val books = _books.asStateFlow()

    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var isLoadingBooksFromRemote by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var isLocallyCached by mutableStateOf(false)
    var bookIdForRequestedChapter = ""

    init {
        viewModelScope.launch {
            ConnectivityStateObserverImpl(application.applicationContext).observe().collect {
                isConnected = it.name == ConnectivityStateObserver.ConnectivityState.AVAILABLE.name
            }
        }
    }

    fun getBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isLoading = true
                }
                var bookList = localRepository.getBooks()

                if (bookList.isNotEmpty()) {
                    isLocallyCached = true
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = false
                    }
                    _books.update { bookList }
                } else {
                    isLocallyCached = false
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = true
                    }
                    bookList = remoteRepository.getBooks()
                    _books.update { bookList }
                    saveBooks(bookList)
                }
                withContext(Dispatchers.Main) {
                    isLoading = false
                    isError = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                    isError = true
                }
                e.message?.let { Log.e("error", it) }
            }
        }
    }

    private suspend fun saveBooks(books: List<Book>) {
        books.map { book -> localRepository.insertBook(book) }
    }
}