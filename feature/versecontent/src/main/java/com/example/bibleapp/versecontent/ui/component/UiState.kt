package com.example.bibleapp.versecontent.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bibleapp.versecontent.ui.viewmodel.VerseContentViewModel

@Composable
fun UIState(
    viewModel: VerseContentViewModel
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (
            viewModel.isLoading && viewModel.isConnected || viewModel.isLoading && viewModel.isLocallyCached
        ) {
            CircularProgressIndicator()
        }

        if (
            !viewModel.isConnected && !viewModel.isLocallyCached
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.WifiOff,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "No internet connection")
            }
        }

        if (
            viewModel.isError && viewModel.isConnected && !viewModel.isLoading
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "Something went wrong")
            }
        }
    }
}