package com.example.bibleapp

import android.content.Context
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.Room
import com.example.bibleapp.database.*
import com.example.bibleapp.network.BibleApiService
import com.example.bibleapp.repository.LocalRepository
import com.example.bibleapp.repository.LocalRepositoryImpl
import com.example.bibleapp.repository.RemoteRepository
import com.example.bibleapp.repository.RemoteRepositoryImp
import com.example.bibleapp.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
