package com.jsoft.f_test.feature.movies.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsoft.f_test.feature.movies.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesContent(
        state = state,
        onRefresh = viewModel::refresh,
        onErrorDismissed = viewModel::dismissError,
    )
}