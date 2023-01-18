package com.example.bibleapp.navigation

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
                enterTransition = {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Down)
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                },
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
                var verseNumber by remember {
                    mutableStateOf(unformattedVerse.substringAfter(':', "no verse").toVerseNumber())
                }

                val verseContent = viewModel.verseContent.collectAsState().value
                bookName = backStackEntry.arguments?.getString("bookName") ?: ""
                chapter = backStackEntry.arguments?.getString("chapter") ?: ""

                leftHeader = if (verseNumber == 0) {
                    "$bookName $chapter"
                } else {
                    "$bookName $chapter:${verseNumber}"
                }

                VerseContentPage(
                    verseContent = verseContent,
                    onClickPrevious = {
                        if (verseNumber != 0) {
                            verseContent.previous?.id?.let { previousVerseId ->
                                viewModel.getVerseContent(
                                    verseId = previousVerseId
                                )
                                viewModel.verseIdForRequestedVerseContent = previousVerseId
                            }
                            if (viewModel.isConnected || viewModel.isLocallyCached) {
                                verseNumber -= 1
                            }
                        }
                    },
                    onClickNext = {
                        if (verseNumber != 0) {
                            verseContent.next?.id?.let { nextVerseId ->
                                viewModel.getVerseContent(
                                    verseId = nextVerseId
                                )
                                viewModel.verseIdForRequestedVerseContent = nextVerseId
                            }
                            if (viewModel.isConnected || viewModel.isLocallyCached) {
                                verseNumber += 1
                            }
                        }
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

fun String.toVerseNumber() = if (this == "no verse") 0 else this.toInt()

class Destinations {
    companion object {
        const val booksPage = "books_page"
        const val chaptersPage = "chapters_page"
        const val versesPage = "verses_page"
        const val verseContentPage = "verse_content_page"
    }
}