package com.example.bibleapp.network

import com.example.bibleapp.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "3ea7b60514775cbc2d69a67027b75e41"
const val header = "api-key: $API_KEY"
const val baseUrl = "https://api.scripture.api.bible/v1/"

val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(baseUrl)
    .build()

interface BibleService {
    @Headers(header)
    @GET("bibles")
    suspend fun getBibles(
        @Query("ids") vararg id: String = arrayOf("de4e12af7f28f599-02")
    ): BibleData

    @Headers(header)
    @GET("bibles/{bibleId}/books")
    suspend fun getBooks(
        @Path("bibleId") bibleID: String = "de4e12af7f28f599-02"
    ): BookData

    @Headers(header)
    @GET("bibles/{bibleId}/books/{bookId}/chapters")
    suspend fun getChapters(
        @Path("bibleId") bibleID: String = "de4e12af7f28f599-02",
        @Path("bookId") bookID: String,
    ): ChapterData

    @Headers(header)
    @GET("bibles/{bibleId}/chapters/{chapterId}/verses")
    suspend fun getVerses(
        @Path("bibleId") bibleID: String = "de4e12af7f28f599-02",
        @Path("chapterId") chapterID: String,
    ): VerseData

    @Headers(header)
    @GET("bibles/{bibleId}/verses/{verseId}")
    suspend fun getVerseContent(
        @Path("bibleId") bibleID: String = "de4e12af7f28f599-02",
        @Path("verseId") verseID: String,
        @Query("content-type") contentType: String = "text",
    ): VerseContentData

    @Headers(header)
    @GET("bibles/{bibleId}/chapters/{chapterId}/sections")
    suspend fun getSections(
        @Path("bibleId") bibleID: String = "de4e12af7f28f599-02",
        @Path("chapterId") chapterId: String
    ): SectionData

    @Headers(header)
    @GET("bibles/{bibleId}/sections/{sectionId}")
    suspend fun getSectionContent(
        @Path("bibleId") bibleID: String,
        @Path("sectionId") sectionId: String,
        @Query("content-type") contentType: String = "text",
        @Query("include-titles") includeTitles: Boolean = true,
    ): SectionContentData

}

object BibleApiService {
    val retrofitApiService: BibleService by lazy {
        retrofit.create(BibleService::class.java)
    }
}