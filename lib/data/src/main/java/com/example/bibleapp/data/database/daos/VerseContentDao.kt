package com.example.bibleapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.bibleapp.data.database.VERSE_CONTENT_TABLE
import com.example.bibleapp.data.database.entity.VerseContentTable

@Dao
interface VerseContentDao {
    @Query(
        """SELECT * FROM $VERSE_CONTENT_TABLE
    WHERE verse_id LIKE '%' || :verseId || '%'
    """
    )
    fun getVerseContent(verseId: String): VerseContentTable?

    @Insert(onConflict = REPLACE)
    fun insertVerseContent(verseContentTable: VerseContentTable)

    @Query("DELETE FROM $VERSE_CONTENT_TABLE")
    fun clearVerses()
}