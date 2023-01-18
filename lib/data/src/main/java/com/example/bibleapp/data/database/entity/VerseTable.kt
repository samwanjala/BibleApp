package com.example.bibleapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibleapp.data.database.VERSE_TABLE

@Entity(tableName = VERSE_TABLE)
data class VerseTable(
    @ColumnInfo("verse_id") @PrimaryKey val verseId: String,
    @ColumnInfo(name = "chapter_id") val chapterId: String?,
    val reference: String?,
)