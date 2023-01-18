package com.example.bibleapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibleapp.data.database.VERSE_CONTENT_TABLE

@Entity(tableName = VERSE_CONTENT_TABLE)
data class VerseContentTable(
    @PrimaryKey(autoGenerate = true) val verseContentId: Int? = null,
    @ColumnInfo(name = "verse_id") val verseId: String?,
    val content: String?,
    @ColumnInfo(name = "next_verse_id") val nextVerseId: String?,
    @ColumnInfo(name = "previous_verse") val previousVerseId: String?,
)