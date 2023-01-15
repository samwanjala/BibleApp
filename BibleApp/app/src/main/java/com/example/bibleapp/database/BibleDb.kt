package com.example.bibleapp.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.bibleapp.data.*

const val BOOK_TABLE = "book_table"
const val CHAPTER_TABLE = "chapter_table"
const val VERSE_TABLE = "verse_table"
const val VERSE_CONTENT_TABLE = "verse_content_table"

@Entity(tableName = BOOK_TABLE)
data class BookTable(
    @ColumnInfo("book_id") @PrimaryKey val bookId: String,
    @ColumnInfo(name = "book_name") val bookName: String?
)

@Entity(tableName = CHAPTER_TABLE)
data class ChapterTable(
    @ColumnInfo("chapter_id") @PrimaryKey val chapterId: String,
    @ColumnInfo(name = "book_id") val bookId: String?,
    @ColumnInfo(name = "chapter_number") val chapterNumber: String?
)

@Entity(tableName = VERSE_TABLE)
data class VerseTable(
    @ColumnInfo("verse_id") @PrimaryKey val verseId: String,
    @ColumnInfo(name = "chapter_id") val chapterId: String?,
    val reference: String?,
)

@Entity(tableName = VERSE_CONTENT_TABLE)
data class VerseContentTable(
    @PrimaryKey(autoGenerate = true) val verseContentId: Int? = null,
    @ColumnInfo(name = "verse_id") val verseId: String?,
    val content: String?,
    @ColumnInfo(name = "next_verse_id") val nextVerseId: String?,
    @ColumnInfo(name = "previous_verse") val previousVerseId: String?,
)

@Database(
    entities = [BookTable::class, ChapterTable::class, VerseTable::class, VerseContentTable::class],
    version = 2,
    exportSchema = false
)
abstract class BibleDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun verseContentDao(): VerseContentDao
}

@Dao
interface BookDao{
    @Query("SELECT * FROM $BOOK_TABLE")
    fun getBooks(): List<BookTable>

    @Insert(onConflict = REPLACE)
    fun insertBook(bookTable: BookTable)
}

@Dao
interface ChapterDao{
    @Query("SELECT * FROM $CHAPTER_TABLE WHERE book_id = :bookId")
    fun getChapters(bookId: String): List<ChapterTable>

    @Insert(onConflict = REPLACE)
    fun insertChapter(chapterTable: ChapterTable)
}

@Dao
interface VerseDao{
    @Query("SELECT * FROM $VERSE_TABLE WHERE chapter_id = :chapterId")
    fun getVerses(chapterId: String): List<VerseTable>

    @Insert(onConflict = REPLACE)
    fun insertVerse(verseTable: VerseTable)
}

@Dao
interface VerseContentDao{
    @Query("SELECT * FROM $VERSE_CONTENT_TABLE WHERE verse_id = :verseId")
    fun getVerseContent(verseId: String): VerseContentTable?

    @Insert(onConflict = REPLACE)
    fun insertVerseContent(verseContentTable: VerseContentTable)

    @Query("DELETE FROM $VERSE_CONTENT_TABLE")
    fun clearVerses()
}

