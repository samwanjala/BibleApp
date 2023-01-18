package com.example.bibleapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bibleapp.data.database.BOOK_TABLE
import com.example.bibleapp.data.database.entity.BookTable

@Dao
interface BookDao {
    @Query("SELECT * FROM $BOOK_TABLE")
    fun getBooks(): List<BookTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(bookTable: BookTable)
}
