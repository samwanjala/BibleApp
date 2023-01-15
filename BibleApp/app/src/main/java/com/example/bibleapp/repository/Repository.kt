package com.example.bibleapp.repository

import com.example.bibleapp.data.*
import com.example.bibleapp.database.*
import com.example.bibleapp.network.BibleApiService

interface RemoteRepository {
    suspend fun getBibles(): List<Bible>
    suspend fun getBooks(): List<Book>
    suspend fun getChapters(bibleId: String = "", bookId: String): List<Chapter>
    suspend fun getVerses(bibleId: String = "", chapterId: String): List<Verse>
    suspend fun getVerseContent(bibleId: String = "", verseId: String): VerseContent
}

class RemoteRepositoryImp(
    private val bibleApiService: BibleApiService
) : RemoteRepository {
    override suspend fun getBibles(): List<Bible> = bibleApiService.retrofitApiService.getBibles().data

    override suspend fun getBooks(): List<Book> = bibleApiService.retrofitApiService.getBooks().data

    override suspend fun getChapters(bibleId: String, bookId: String): List<Chapter> =
        bibleApiService.retrofitApiService.getChapters(bookID = bookId).data

    override suspend fun getVerses(bibleId: String, chapterId: String): List<Verse> =
        bibleApiService.retrofitApiService.getVerses(chapterID = chapterId).data

    override suspend fun getVerseContent(bibleId: String, verseId: String): VerseContent =
        bibleApiService.retrofitApiService.getVerseContent(verseID = verseId).data
}

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

class LocalRepositoryImpl(
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao,
    private val verseContentDao: VerseContentDao
) : LocalRepository {
    override suspend fun getBooks(): List<Book> =
        bookDao.getBooks().map { value -> value.toBook()}
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

fun BookTable.toBook(): Book {
    return Book(
        id = this.bookId,
        bibleId = "",
        name = this.bookName ?: "",
        nameLong = ""
    )
}

fun Book.toBookTable(): BookTable {
    return BookTable(
        bookId = this.id,
        bookName = this.name
    )
}

fun ChapterTable.toChapter(): Chapter {
    return Chapter(
        id = this.chapterId,
        bibleId = "",
        number = this.chapterNumber ?: "",
        bookId = this.bookId ?: "",
        reference = ""
    )
}

fun Chapter.toChapterTable(): ChapterTable {
    return ChapterTable(
        chapterId = this.id,
        bookId = this.bookId,
        chapterNumber = this.number
    )
}

fun VerseTable.toVerse(): Verse {
    return Verse(
        id = this.verseId,
        orgId = "",
        bibleId = "",
        bookId = "",
        chapterId = this.chapterId ?: "",
        reference = this.reference ?: ""
    )
}

fun Verse.toVerseTable(): VerseTable {
    return VerseTable(
        verseId = this.id,
        chapterId = this.chapterId,
        reference = this.reference
    )
}

fun VerseContentTable?.toVerseContent(): VerseContent? {
    if (this == null) return null
    return VerseContent(
        id = this.verseId,
        content = this.content,
        verseCount = null,
        next = NextVerse(id = this.nextVerseId, bookId = ""),
        previous = PreviousVerse(id = this.previousVerseId, bookId = "")
    )
}

fun VerseContent.toVerseContentTable(): VerseContentTable {
    return VerseContentTable(
        verseId = this.id ?: "",
        content = this.content,
        nextVerseId = this.next?.id,
        previousVerseId = this.previous?.id
    )
}

