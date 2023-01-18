package com.example.bibleapp.verse.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibleapp.domain.model.Verse
import com.example.bibleapp.domain.repository.LocalRepository
import com.example.bibleapp.domain.repository.RemoteRepository
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
class VerseViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val application: Application
) : ViewModel() {
    private val _verses = MutableStateFlow(emptyList<Verse>())
    val verses = _verses.asStateFlow()
    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var isLoadingVersesFromRemote by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var verseIdForRequestedVerseContent = ""
    var isLocallyCached by mutableStateOf(false)

    init {
        viewModelScope.launch {
            ConnectivityStateObserverImpl(application.applicationContext).observe().collect {
                isConnected = it.name == ConnectivityStateObserver.ConnectivityState.AVAILABLE.name
            }
        }
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
                        isLoadingVersesFromRemote = false
                    }
                    _verses.update { verseList }
                } else {
                    isLocallyCached = false
                    _verses.update { emptyList() }
                    withContext(Dispatchers.Main) {
                        isLoadingVersesFromRemote = true
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
}