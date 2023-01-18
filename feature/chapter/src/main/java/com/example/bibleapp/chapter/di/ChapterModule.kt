package com.example.bibleapp.chapter.di

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
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChapterModule{
    @Provides
    fun provideBibleApiService() = BibleApiService

    @Provides
    fun provideLocalRepository(
        bookDao: BookDao,
        chapterDao: ChapterDao,
        verseDao: VerseDao,
        verseContentDao: VerseContentDao
    ): LocalRepository = LocalRepositoryImpl(bookDao, chapterDao, verseDao, verseContentDao)

    @Provides
    fun provideRemoteRepository(
        bibleApiService: BibleApiService
    ): RemoteRepository = RemoteRepositoryImp(bibleApiService)

}