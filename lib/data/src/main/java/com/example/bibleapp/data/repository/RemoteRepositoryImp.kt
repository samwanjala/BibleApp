package com.example.bibleapp.data.repository

import com.example.bibleapp.data.*
import com.example.bibleapp.data.network.BibleApiService
import com.example.bibleapp.domain.model.*
import com.example.bibleapp.domain.repository.RemoteRepository

class RemoteRepositoryImp(
    private val bibleApiService: BibleApiService
) : RemoteRepository {
    override suspend fun getBibles(): List<Bible> =
        bibleApiService.retrofitApiService.getBibles().data

    override suspend fun getBooks(): List<Book> = bibleApiService.retrofitApiService.getBooks().data

    override suspend fun getChapters(bibleId: String, bookId: String): List<Chapter> =
        bibleApiService.retrofitApiService.getChapters(bookID = bookId).data

    override suspend fun getVerses(bibleId: String, chapterId: String): List<Verse> =
        bibleApiService.retrofitApiService.getVerses(chapterID = chapterId).data

    override suspend fun getVerseContent(bibleId: String, verseId: String): VerseContent =
        bibleApiService.retrofitApiService.getVerseContent(verseID = verseId).data
}