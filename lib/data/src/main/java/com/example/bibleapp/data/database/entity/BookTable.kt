package com.example.bibleapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bibleapp.data.database.BOOK_TABLE

@Entity(tableName = BOOK_TABLE)
data class BookTable(
    @ColumnInfo("book_id") @PrimaryKey val bookId: String,
    @ColumnInfo(name = "book_name") val bookName: String?
)