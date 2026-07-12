package com.jsoft.f_test.feature.movies.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jsoft.f_test.core.ui.theme.F_testTheme
import com.jsoft.f_test.feature.movies.uistate.MovieUi
import com.jsoft.f_test.feature.movies.uistate.MoviesUiState

@Composable
fun MoviesContent(
    state: MoviesUiState,
    onRefresh: () -> Unit,
    onErrorDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null && state.movies.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.errorMessage)
            onErrorDismissed()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isInitialLoading -> CenteredLoading()
            state.isEmpty -> EmptyState(onRefresh = onRefresh)
            state.movies.isEmpty() && state.errorMessage != null -> {
                ErrorState(message = state.errorMessage, onRetry = onRefresh)
            }

            else -> MoviesList(
                movies = state.movies,
                isRefreshing = state.isRefreshing,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun CenteredLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.Movie,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Kinolar topilmadi",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRefresh) { Text("Qayta yuklash") }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Xatolik yuz berdi",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Qayta urinish") }
    }
}

@Composable
private fun MoviesList(
    movies: List<MovieUi>,
    isRefreshing: Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = movies, key = { it.id }) { movie ->
                MovieCard(movie = movie)
            }
        }

        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .height(24.dp),
                strokeWidth = 2.dp,
            )
        }
    }
}

@Composable
private fun MovieCard(movie: MovieUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = movie.ratingText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = movie.releaseYearText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private val sampleMovies = listOf(
    MovieUi(
        id = 1,
        title = "Inception",
        posterUrl = null,
        ratingText = "8.8",
        releaseYearText = "2010",
        overview = "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible.",
    ),
    MovieUi(
        id = 2,
        title = "Interstellar",
        posterUrl = null,
        ratingText = "8.6",
        releaseYearText = "2014",
        overview = "The adventures of a group of explorers who make use of a newly discovered rift in space-time.",
    ),
)

@Preview(showBackground = true)
@Composable
private fun MoviesListPreview() {
    F_testTheme {
        MoviesContent(
            state = MoviesUiState(movies = sampleMovies),
            onRefresh = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialLoadingPreview() {
    F_testTheme {
        MoviesContent(
            state = MoviesUiState(isRefreshing = true),
            onRefresh = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    F_testTheme {
        MoviesContent(
            state = MoviesUiState(),
            onRefresh = {},
            onErrorDismissed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    F_testTheme {
        MoviesContent(
            state = MoviesUiState(errorMessage = "Internet aloqasi yo'q"),
            onRefresh = {},
            onErrorDismissed = {},
        )
    }
}