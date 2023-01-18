package com.example.bibleapp.versecontent.ui.viewmodel

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VerseContentViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val application: Application
) : ViewModel() {
    private val _verseContent = MutableStateFlow(VerseContent())
    val verseContent = _verseContent.asStateFlow()
    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var isLoadingVerseContentFromRemote by mutableStateOf(false)
    var isConnected by mutableStateOf(false)
    var isLocallyCached by mutableStateOf(false)
    var verseIdForRequestedVerseContent = ""

    init {
        viewModelScope.launch {
            ConnectivityStateObserverImpl(application.applicationContext).observe().collect {
                isConnected = it.name == ConnectivityStateObserver.ConnectivityState.AVAILABLE.name
            }
        }
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
                        isLoadingVerseContentFromRemote = false
                    }
                    _verseContent.update { verseCont!! }
                } else {
                    isLocallyCached = false
                    _verseContent.update { VerseContent() }
                    withContext(Dispatchers.Main) {
                        isLoadingVerseContentFromRemote = true
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