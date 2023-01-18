package com.example.bibleapp.chapter.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibleapp.domain.model.*
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

class ChapterViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val application: Application
): ViewModel() {
    private val _chapters = MutableStateFlow(emptyList<Chapter>())
    val chapters = _chapters.asStateFlow()
    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var isLoadingBooksFromRemote by mutableStateOf(false)
    var isLoadingChaptersFromRemote by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var chapterIdForRequestedVerse = ""
    var isLocallyCached by mutableStateOf(false)

    init {
        viewModelScope.launch {
            ConnectivityStateObserverImpl(application.applicationContext).observe().collect {
                isConnected = it.name == ConnectivityStateObserver.ConnectivityState.AVAILABLE.name
            }
        }
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

}