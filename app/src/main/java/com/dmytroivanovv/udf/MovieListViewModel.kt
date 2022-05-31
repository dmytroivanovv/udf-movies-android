package com.dmytroivanovv.udf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dmytroivanovv.core.CoroutineDispatchers
import com.dmytroivanovv.core.favoriteMoviesRepository.FavoriteMoviesRepository
import com.dmytroivanovv.core.moviePresentationRepository.MoviePresentationRepository
import com.dmytroivanovv.core.moviePresentationRepository.MoviePresentationType
import com.dmytroivanovv.core.movieUseCase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MovieListViewModel {

    val moviesViewStates: LiveData<List<MovieListUiItem>>

    val presentationType: LiveData<MoviePresentationType>

    fun onFavoriteClicked(movie: MovieUiModel)

    fun onRetryClicked()

    fun onChangeVisualPresentationClicked()
}

@HiltViewModel
class MovieListViewModelImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
    private val moviePresentationRepository: MoviePresentationRepository
) : ViewModel(), MovieListViewModel {

    private var downloadMovieListJob: Job? = null

    override val moviesViewStates =
        MutableLiveData<List<MovieListUiItem>>(listOf(MovieListUiItem.Loading))

    override val presentationType = moviePresentationRepository.type.asLiveData()

    init {
        downloadMovieList()
    }

    private fun downloadMovieList() {
        downloadMovieListJob?.cancel()
        downloadMovieListJob = viewModelScope.launch(coroutineDispatchers.default) {
            combine(
                getMoviesUseCase.getMovies(),
                moviePresentationRepository.type
            ) { moviesResult, presentationType ->
                MovieListViewModelUtil.mapToUiStates(
                    moviesResult = moviesResult,
                    presentationType = presentationType
                )
            }.onStart {
                moviesViewStates.postValue(listOf(MovieListUiItem.Loading))
            }.collect { viewStates ->
                moviesViewStates.postValue(viewStates)
            }
        }
    }

    override fun onFavoriteClicked(movie: MovieUiModel) {
        viewModelScope.launch(coroutineDispatchers.io) {
            if (movie.favorite) {
                favoriteMoviesRepository.remove(movie.id)
            } else {
                favoriteMoviesRepository.add(movie.id)
            }
        }
    }

    override fun onRetryClicked() {
        downloadMovieList()
    }

    override fun onChangeVisualPresentationClicked() {
        viewModelScope.launch {
            val newType = when (moviePresentationRepository.type.value) {
                MoviePresentationType.LINEAR -> MoviePresentationType.GRID
                MoviePresentationType.GRID -> MoviePresentationType.LINEAR
            }
            moviePresentationRepository.set(newType = newType)
        }
    }
}