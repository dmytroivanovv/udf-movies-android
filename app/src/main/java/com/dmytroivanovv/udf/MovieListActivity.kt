package com.dmytroivanovv.udf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.dmytroivanovv.core.moviePresentationRepository.MoviePresentationType
import com.dmytroivanovv.udf.ui.theme.UnidirectionalDataFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListActivity : ComponentActivity() {

    private val viewModel: MovieListViewModel by viewModels<MovieListViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnidirectionalDataFlowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MovieListScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MovieListScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel
) {
    val moviesViewStates: List<MovieListUiItem> by viewModel.moviesViewStates.observeAsState(
        emptyList()
    )

    val presentationType: MoviePresentationType by viewModel.presentationType.observeAsState(
        MoviePresentationType.LINEAR
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.movie_toolbar_title))
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, "")
                    }
                },
                actions = {
                    IconButton({
                        viewModel.onChangeVisualPresentationClicked()
                    }) {
                        Icon(
                            painter = painterResource(
                                when (presentationType) {
                                    MoviePresentationType.LINEAR ->
                                        R.drawable.ic_baseline_grid_on_24
                                    MoviePresentationType.GRID ->
                                        R.drawable.ic_baseline_view_list_24
                                }
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
        }, content = {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                moviesViewStates.forEach { uiState ->
                    item {
                        when (uiState) {
                            MovieListUiItem.Empty -> Text(text = stringResource(id = R.string.movie_list_empty))
                            is MovieListUiItem.Error -> Text(text = uiState.text)
                            is MovieListUiItem.GridMovies -> GridMovies(
                                movie1 = uiState.movie1,
                                movie2 = uiState.movie2
                            )
                            is MovieListUiItem.LinearMovie -> LinearMovie(movie = uiState.movie)
                            MovieListUiItem.Loading -> CircularProgressIndicator()
                        }
                    }
                }
            }
        })
}

@Composable
fun LinearMovie(
    modifier: Modifier = Modifier,
    movie: MovieUiModel
) {
    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .width(130.dp),
            contentScale = ContentScale.FillBounds,
            model = movie.imageUrl,
            contentDescription = movie.title
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(text = movie.title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Text(text = movie.year)
            Text(text = movie.description)
        }
    }
}

@Composable
fun GridMovies(
    modifier: Modifier = Modifier,
    movie1: MovieUiModel,
    movie2: MovieUiModel?
) {
    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .height(280.dp)
                .weight(1f),
            contentScale = ContentScale.FillBounds,
            model = movie1.imageUrl,
            contentDescription = movie1.title
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .height(280.dp)
                .weight(1f)
        ) {
            if (movie2 != null) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    model = movie2.imageUrl,
                    contentDescription = movie2.title
                )
            }
        }
    }
}