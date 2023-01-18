package com.example.bibleapp.data.repository

import com.example.bibleapp.data.database.daos.BookDao
import com.example.bibleapp.data.database.daos.ChapterDao
import com.example.bibleapp.data.database.daos.VerseContentDao
import com.example.bibleapp.data.database.daos.VerseDao
import com.example.bibleapp.domain.model.Book
import com.example.bibleapp.domain.model.Chapter
import com.example.bibleapp.domain.model.Verse
import com.example.bibleapp.domain.model.VerseContent
import com.example.bibleapp.domain.repository.LocalRepository


class LocalRepositoryImpl(
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao,
    private val verseContentDao: VerseContentDao
) : LocalRepository {
    override suspend fun getBooks(): List<Book> =
        bookDao.getBooks().map { value -> value.toBook() }

    override suspend fun insertBook(book: Book) =
        bookDao.insertBook(book.toBookTable())

    override suspend fun getChapters(bookId: String): List<Chapter> =
        chapterDao.getChapters(bookId).map { value -> value.toChapter() }

    override suspend fun insertChapter(chapter: Chapter) =
        chapterDao.insertChapter(chapter.toChapterTable())

    override suspend fun getVerses(chapterId: String): List<Verse> =
        verseDao.getVerses(chapterId).map { value -> value.toVerse() }

    override suspend fun insertVerse(verse: Verse) =
        verseDao.insertVerse(verse.toVerseTable())

    override suspend fun getVerseContent(verseId: String): VerseContent? =
        verseContentDao.getVerseContent(verseId)?.toVerseContent()

    override suspend fun insertVerseContent(verseContent: VerseContent) =
        verseContentDao.insertVerseContent(verseContent.toVerseContentTable())

    override suspend fun clearVerses() = verseContentDao.clearVerses()
}