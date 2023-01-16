package com.example.bibleapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibleapp.data.*
import com.example.bibleapp.repository.LocalRepository
import com.example.bibleapp.repository.RemoteRepository
import com.example.bibleapp.util.ConnectivityStateObserver
import com.example.bibleapp.util.ConnectivityStateObserverImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    val application: Application
) : ViewModel() {
    private val _bibles = MutableStateFlow(emptyList<Bible>())
    val bibles = _bibles.asStateFlow()
    private val _books = MutableStateFlow(emptyList<Book>())
    val books = _books.asStateFlow()
    private val _chapters = MutableStateFlow(emptyList<Chapter>())
    val chapters = _chapters.asStateFlow()
    private val _verses = MutableStateFlow(emptyList<Verse>())
    val verses = _verses.asStateFlow()
    private val _verseContent = MutableStateFlow(VerseContent())
    val verseContent = _verseContent.asStateFlow()
    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var isLoadingBooksFromRemote by mutableStateOf(false)
    var isLoadingChaptersFromRemote by mutableStateOf(false)
    var isLoadingVersesFromRemote by mutableStateOf(false)
    var isLoadingVerseContentFromRemote by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var chapterIdForRequestedVerse = ""
    var bookIdForRequestedChapter = ""
    var verseIdForRequestedVerseContent = ""
    var isLocallyCached by mutableStateOf(false)

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

    fun getChapters(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isLoading = true
                }
                var chapterList = localRepository.getChapters(bookId)

                if (chapterList.isNotEmpty()) {
                    isLocallyCached = true
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = false
                    }
                    _chapters.update { chapterList }
                } else {
                    isLocallyCached = false
                    _chapters.update { emptyList() }
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = true
                    }
                    chapterList = remoteRepository.getChapters(bookId = bookId)
                    _chapters.update{ chapterList }
                    saveChapters(chapterList)
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

    private suspend fun saveChapters(chapters: List<Chapter>) {
        chapters.map { chapter -> localRepository.insertChapter(chapter) }
    }

    fun getVerses(chapterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isLoading = true
                }
                var verseList = localRepository.getVerses(chapterId)

                if (verseList.isNotEmpty()) {
                    isLocallyCached = true
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = false
                    }
                    _verses.update { verseList }
                } else {
                    isLocallyCached = false
                    _verses.update { emptyList() }
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = true
                    }
                    verseList = remoteRepository.getVerses(chapterId = chapterId)
                    _verses.update { verseList }
                    saveVerses(verseList)
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

    private suspend fun saveVerses(verses: List<Verse>) {
        verses.map { verse -> localRepository.insertVerse(verse) }
    }

    fun getVerseContent(
        verseId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isLoading = true
                }
                var verseCont = localRepository.getVerseContent(verseId = verseId)
                if (verseCont != null){
                    isLocallyCached = true
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = false
                    }
                    _verseContent.update { verseCont!! }
                } else {
                    isLocallyCached = false
                    _verseContent.update { VerseContent() }
                    withContext(Dispatchers.Main) {
                        isLoadingBooksFromRemote = true
                    }
                    verseCont = remoteRepository.getVerseContent(verseId = verseId)
                    _verseContent.update { verseCont }
                    saveVerseContent(verseCont)
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

    private suspend fun saveVerseContent(verseContent: VerseContent) {
        localRepository.insertVerseContent(verseContent)
    }

    private fun deleteAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                localRepository.clearVerses()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
                e.message?.let { Log.e("error", it) }
            }
        }
    }
}