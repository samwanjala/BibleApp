package com.example.bibleapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibleapp.data.database.CHAPTER_TABLE

@Entity(tableName = CHAPTER_TABLE)
data class ChapterTable(
    @ColumnInfo("chapter_id") @PrimaryKey val chapterId: String,
    @ColumnInfo(name = "book_id") val bookId: String?,
    @ColumnInfo(name = "chapter_number") val chapterNumber: String?
)