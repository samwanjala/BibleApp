package com.example.bibleapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bibleapp.data.database.VERSE_TABLE
import com.example.bibleapp.data.database.entity.VerseTable

@Dao
interface VerseDao {
    @Query("SELECT * FROM $VERSE_TABLE WHERE chapter_id = :chapterId")
    fun getVerses(chapterId: String): List<VerseTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVerse(verseTable: VerseTable)
}