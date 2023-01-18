package com.example.bibleapp.domain.repository

import com.example.bibleapp.domain.model.*

interface RemoteRepository {
    suspend fun getBibles(): List<Bible>
    suspend fun getBooks(): List<Book>
    suspend fun getChapters(bibleId: String = "", bookId: String): List<Chapter>
    suspend fun getVerses(bibleId: String = "", chapterId: String): List<Verse>
    suspend fun getVerseContent(bibleId: String = "", verseId: String): VerseContent
}

