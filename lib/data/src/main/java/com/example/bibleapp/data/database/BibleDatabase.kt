package com.example.bibleapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bibleapp.data.database.daos.BookDao
import com.example.bibleapp.data.database.daos.ChapterDao
import com.example.bibleapp.data.database.daos.VerseContentDao
import com.example.bibleapp.data.database.daos.VerseDao
import com.example.bibleapp.data.database.entity.BookTable
import com.example.bibleapp.data.database.entity.ChapterTable
import com.example.bibleapp.data.database.entity.VerseContentTable
import com.example.bibleapp.data.database.entity.VerseTable


@Database(
    entities = [
        BookTable::class,
        ChapterTable::class,
        VerseTable::class,
        VerseContentTable::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun verseContentDao(): VerseContentDao
}