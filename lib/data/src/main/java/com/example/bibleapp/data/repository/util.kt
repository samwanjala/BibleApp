package com.example.bibleapp.data.repository

import com.example.bibleapp.data.*
import com.example.bibleapp.data.database.entity.BookTable
import com.example.bibleapp.data.database.entity.ChapterTable
import com.example.bibleapp.data.database.entity.VerseContentTable
import com.example.bibleapp.data.database.entity.VerseTable
import com.example.bibleapp.domain.model.*


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