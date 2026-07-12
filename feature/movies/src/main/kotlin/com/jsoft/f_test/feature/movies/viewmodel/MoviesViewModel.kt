package com.jsoft.f_test.feature.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.movies.usecase.GetMoviesUseCase
import com.jsoft.f_test.domain.movies.usecase.RefreshMoviesUseCase
import com.jsoft.f_test.feature.movies.uistate.MoviesUiState
import com.jsoft.f_test.feature.movies.uistate.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getMovies: GetMoviesUseCase,
    private val refreshMovies: RefreshMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getMovies()
            .onEach { movies ->
                _uiState.update { state ->
                    state.copy(movies = movies.map { it.toUi() })
                }
            }
            .launchIn(viewModelScope)

        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }

            when (val result = refreshMovies()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            errorMessage = result.error.message ?: "Xatolik yuz berdi",
                        )
                    }
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}