package com.example.bibleapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.bibleapp.data.database.BibleDatabase
import com.example.bibleapp.data.database.daos.BookDao
import com.example.bibleapp.data.database.daos.ChapterDao
import com.example.bibleapp.data.database.daos.VerseContentDao
import com.example.bibleapp.data.database.daos.VerseDao
import com.example.bibleapp.data.network.BibleApiService
import com.example.bibleapp.data.repository.LocalRepositoryImpl
import com.example.bibleapp.data.repository.RemoteRepositoryImp
import com.example.bibleapp.domain.repository.LocalRepository
import com.example.bibleapp.domain.repository.RemoteRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

const val dbName = "Bible Database"

@Module
@InstallIn(SingletonComponent::class)
object DBModule{
    @Provides
    fun provideBookDao(
        bibleDB: BibleDatabase
    ) = bibleDB.bookDao()

    @Provides
    fun provideChapterDao(
        bibleDB: BibleDatabase
    ) = bibleDB.chapterDao()

    @Provides
    fun provideVerseDao(
        bibleDB: BibleDatabase
    ) = bibleDB.verseDao()

    @Provides
    fun provideVerseContentDao(
        bibleDB: BibleDatabase
    ) = bibleDB.verseContentDao()

    @Provides
    fun provideBibleApiService() = BibleApiService

    @Provides
    fun provideDatabaseRepository(
        bookDao: BookDao,
        chapterDao: ChapterDao,
        verseDao: VerseDao,
        verseContentDao: VerseContentDao
    ): LocalRepository = LocalRepositoryImpl(bookDao, chapterDao, verseDao, verseContentDao)

    @Provides
    fun provideRemoteRepository(
        bibleApiService: BibleApiService
    ): RemoteRepository = RemoteRepositoryImp(bibleApiService)

    @Provides
    fun provideBibleDB(
        @ApplicationContext context : Context
    ) = Room.databaseBuilder(
        context,
        BibleDatabase::class.java,
        dbName
    ).fallbackToDestructiveMigration()
        .build()
}