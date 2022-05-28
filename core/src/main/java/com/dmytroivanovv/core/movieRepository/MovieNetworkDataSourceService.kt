package com.dmytroivanovv.core.movieRepository

import retrofit2.http.GET

interface MovieNetworkDataSourceService {

    @GET("dataset.json")
    suspend fun getMovies(): List<MovieDTO>
}