package com.dmytroivanovv.udf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dmytroivanovv.core.CoroutineDispatchers
import com.dmytroivanovv.core.favoriteMoviesRepository.FavoriteMoviesRepository
import com.dmytroivanovv.core.moviePresentationRepository.ModeRepository
import com.dmytroivanovv.core.moviePresentationRepository.Mode
import com.dmytroivanovv.core.movieUseCase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MovieListViewModel {

    val movieViewStates: LiveData<List<MovieListUiItem>>

    val mode: LiveData<Mode>

    fun onFavoriteClicked(movie: MovieUiModel)

    fun onRetryClicked()

    fun onChangeModeClicked()
}

@HiltViewModel
class MovieListViewModelImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
    private val modeRepository: ModeRepository
) : ViewModel(), MovieListViewModel {

    private var downloadMovieListJob: Job? = null

    override val movieViewStates =
        MutableLiveData<List<MovieListUiItem>>(listOf(MovieListUiItem.Loading))

    override val mode = modeRepository.type.asLiveData()

    init {
        downloadMovieList()
    }

    private fun downloadMovieList() {
        downloadMovieListJob?.cancel()
        downloadMovieListJob = viewModelScope.launch {
            combine(
                getMoviesUseCase.getMovies(),
                modeRepository.type
            ) { moviesResult, presentationType ->
                MovieListViewModelUtil.mapToUiStates(
                    moviesResult = moviesResult,
                    presentationType = presentationType
                )
            }.onStart {
                movieViewStates.postValue(listOf(MovieListUiItem.Loading))
            }.collect { viewStates ->
                movieViewStates.postValue(viewStates)
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

    override fun onChangeModeClicked() {
        viewModelScope.launch {
            val newType = when (modeRepository.type.value) {
                Mode.LIST -> Mode.GRID
                Mode.GRID -> Mode.LIST
            }
            modeRepository.set(newType = newType)
        }
    }
}