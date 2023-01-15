package com.example.bibleapp.navigation

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.navArgument
import com.example.bibleapp.viewmodel.MainViewModel
import com.example.bibleapp.components.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    viewModel: MainViewModel
) {
    val navHostController = rememberAnimatedNavController()
    var leftHeader by remember {
        mutableStateOf("")
    }
    val rightHeader by remember {
        mutableStateOf("King James Version")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        leftHeader
                    )
                    Text(rightHeader)
                }
            }
        }
    ) { paddingValue ->
        val padding = paddingValue

        AnimatedNavHost(
            navController = navHostController,
            startDestination = Destinations.booksPage
        ) {
            composable(
                route = Destinations.booksPage,
                arguments = listOf(navArgument("bookName") {})
            ) {
                leftHeader = ""
                BooksPage(
                    viewModel = viewModel,
                    onClickItem = { _, bookId, bookName ->
                        navHostController.navigate(
                            route = "${Destinations.chaptersPage}/$bookName"
                        ) {
                            launchSingleTop = true
                        }
                        viewModel.bookIdForRequestedChapter = bookId
                    }
                )
            }

            composable(
                route = "${Destinations.chaptersPage}/{bookName}",
                enterTransition = {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
                }
            ) { backStackEntry ->
                val bookName = backStackEntry.arguments?.getString("bookName") ?: "Book"
                leftHeader = bookName
                ChaptersPage(
                    viewModel = viewModel,
                    onClickItem = { _, chapterId, chapter ->
                        navHostController.navigate(
                            route = "${Destinations.versesPage}/$bookName/$chapter"
                        ) {
                            launchSingleTop = true
                        }
                        viewModel.chapterIdForRequestedVerse = chapterId
                    }
                )
            }

            composable(
                route = "${Destinations.versesPage}/{bookName}/{chapter}",
                enterTransition = {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
                }
            ) { backStackEntry ->
                val bookName = backStackEntry.arguments?.getString("bookName") ?: ""
                val chapter = backStackEntry.arguments?.getString("chapter") ?: ""
                leftHeader = "$bookName $chapter"
                VersesPage(
                    viewModel = viewModel,
                    onClickItem = { _, verseId, verse ->
                        navHostController.navigate(
                            route = "${Destinations.verseContentPage}/$verse/$chapter/$bookName"
                        ) {
                            launchSingleTop = true
                        }
                        viewModel.verseIdForRequestedVerseContent = verseId
                    }
                )
            }

            composable(
                route = "${Destinations.verseContentPage}/{verse}/{chapter}/{bookName}",
                enterTransition = {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
                }
            ) { backStackEntry ->
                var bookName by remember {
                    mutableStateOf("")
                }

                var chapter by remember {
                    mutableStateOf("")
                }

                val unformattedVerse = backStackEntry.arguments?.getString("verse") ?: ""
                var verse by remember {
                    mutableStateOf(unformattedVerse.substringAfter(':').toInt())
                }

                val verseContent = viewModel.verseContent.collectAsState().value
                bookName = backStackEntry.arguments?.getString("bookName") ?: ""
                chapter = backStackEntry.arguments?.getString("chapter") ?: ""

                leftHeader = "$bookName $chapter:${verse}"

                Log.d("verse", "$verse")

                VerseContentPage(
                    verseContent = verseContent,
                    onClickPrevious = {
                        verseContent.previous?.id?.let { previousVerseId ->
                            viewModel.getVerseContent(
                                verseId = previousVerseId
                            )
                        }
                        verse -= 1
                    },
                    onClickNext = {
                        verseContent.next?.id?.let { nextVerseId ->
                            viewModel.getVerseContent(
                                verseId = nextVerseId
                            )
                        }
                        verse += 1
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

class Destinations {
    companion object {
        const val booksPage = "books_page"
        const val chaptersPage = "chapters_page"
        const val versesPage = "verses_page"
        const val verseContentPage = "verse_content_page"
    }
}