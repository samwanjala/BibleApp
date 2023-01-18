package com.example.bibleapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bibleapp.data.database.CHAPTER_TABLE
import com.example.bibleapp.data.database.entity.ChapterTable

@Dao
interface ChapterDao {
    @Query("SELECT * FROM $CHAPTER_TABLE WHERE book_id = :bookId")
    fun getChapters(bookId: String): List<ChapterTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapter(chapterTable: ChapterTable)
}
