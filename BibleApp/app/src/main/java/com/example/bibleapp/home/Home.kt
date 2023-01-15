package com.example.bibleapp.home

import androidx.compose.runtime.Composable
import com.example.bibleapp.viewmodel.MainViewModel
import com.example.bibleapp.navigation.AppNavigation

@Composable
fun Home(
    viewModel: MainViewModel
) {
    AppNavigation(viewModel)
}





