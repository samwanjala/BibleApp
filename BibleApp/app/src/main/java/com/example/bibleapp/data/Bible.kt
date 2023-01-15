package com.example.bibleapp.data

import androidx.annotation.Keep

@Keep
data class BibleData(
    val data: List<Bible>
)

@Keep
data class Bible(
    val id: String,
    val name: String,
    val abbreviation: String
)

@Keep
data class BookData(
    val data:List<Book>
)

@Keep
data class Book(
    val id: String,
    val bibleId: String,
    val name: String,
    val nameLong: String
)

@Keep
data class ChapterData(
    val data:List<Chapter>
)

@Keep
data class Chapter(
    val id: String,
    val bibleId: String,
    val number: String,
    val bookId: String,
    val reference: String
)

@Keep
data class VerseData(
    val data: List<Verse>
)

@Keep
data class Verse(
    val id: String,
    val orgId: String,
    val bibleId: String,
    val bookId: String,
    val chapterId: String,
    val reference: String
)

@Keep
data class VerseContentData(
    val data: VerseContent
)

@Keep
data class VerseContent(
    val id: String?,
    val content: String?,
    val verseCount: String?,
    val next: NextVerse?,
    val previous: PreviousVerse?
)

@Keep
data class NextVerse(
    val id: String?,
    val bookId: String?
)

@Keep
data class PreviousVerse(
    val id: String?,
    val bookId: String?
)

@Keep
data class SectionData(
    val data: List<Section>
)

@Keep
data class Section(
    val id: String,
    val bibleId: String,
    val title: String,
    val firstVerseId: String,
    val lastVerseId: String
)

@Keep
data class SectionContentData(
    val data: SectionContent
)

@Keep
data class SectionContent(
    val id: String? = null,
    val bibleId: String? = null,
    val bookId: String? = null,
    val chapterId: String? = null,
    val title: String? = null,
    val content: String? = null,
    val next: NextSection? = null,
    val previous: PreviousSection? = null
)

@Keep
data class NextSection(
    val id: String?,
    val title: String?
)

@Keep
data class PreviousSection(
    val id: String?,
    val title: String?
)