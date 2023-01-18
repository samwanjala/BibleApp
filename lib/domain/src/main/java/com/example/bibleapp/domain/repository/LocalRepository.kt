package com.example.bibleapp.domain.repository

import com.example.bibleapp.domain.model.Book
import com.example.bibleapp.domain.model.Chapter
import com.example.bibleapp.domain.model.Verse
import com.example.bibleapp.domain.model.VerseContent

interface LocalRepository {
    suspend fun getBooks(): List<Book>
    suspend fun insertBook(book: Book)
    suspend fun getChapters(bookId: String): List<Chapter>
    suspend fun insertChapter(chapter: Chapter)
    suspend fun getVerses(chapterId: String): List<Verse>
    suspend fun insertVerse(verse: Verse)
    suspend fun getVerseContent(verseId: String): VerseContent?
    suspend fun insertVerseContent(verseContent: VerseContent)
    suspend fun clearVerses()
}