package com.studio.king.demomovie.network.api

import com.google.gson.JsonObject
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.model.dto.CommentModelDTO
import com.studio.king.demomovie.model.dto.TrendingModelDTO
import com.studio.king.demomovie.utils.Constants
import retrofit2.http.*
import kotlin.collections.HashMap


interface ApiService {

    @Headers(
            "Content-Type: application/json",
            "accept: application/json"
    )
    @GET("${Constants.BASE_API}/genre/movie/list")
    suspend fun getListGenres(@QueryMap optionsQuery: HashMap<String, Any>): JsonObject

    @Headers(
        "Content-Type: application/json",
        "accept: application/json"
    )
    @GET("${Constants.BASE_API}/trending/all/week")
    suspend fun getListTrendingWeek(@QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO


    @Headers(
        "Content-Type: application/json",
        "accept: application/json"
    )
    @GET("${Constants.BASE_API}/movie/popular")
    suspend fun getListMoviePopular(@QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO
//    https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1

    @Headers(
            "Content-Type: application/json",
            "accept: application/json"
    )
    @GET("${Constants.BASE_API}/movie/top_rated")
    suspend fun getListMovieTopRated(@QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO
//    https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1

    @Headers(
            "Content-Type: application/json",
            "accept: application/json"
    )
    @GET("${Constants.BASE_API}/movie/upcoming")
    suspend fun getListMovieUpcoming(@QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO
//    https://api.themoviedb.org/3/movie/upcoming?api_key=<<api_key>>&language=en-US&page=1

    @GET("${Constants.BASE_API}/movie/{movie_id}")
    suspend fun getDetailMovie(@Path("movie_id") movie_id : String, @QueryMap optionsQuery: HashMap<String, Any>): MovieModel
//    https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US

    @GET("${Constants.BASE_API}/movie/{movie_id}/recommendations")
    suspend fun getListMovieRecommendations(@Path("movie_id") movie_id : String, @QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO
//    https://api.themoviedb.org/3/movie/{movie_id}/recommendations?api_key=<<api_key>>&language=en-US&page=1.


    @GET("${Constants.BASE_API}/movie/{movie_id}/lists")
    suspend fun getListMovieDetail(@Path("movie_id") movie_id : String, @QueryMap optionsQuery: HashMap<String, Any>): TrendingModelDTO
//    https://api.themoviedb.org/3/movie/{movie_id}/lists?api_key=<<api_key>>&language=en-US&page=1

    @GET("${Constants.BASE_API}/movie/{movie_id}/credits")
    suspend fun getListMovieCast(@Path("movie_id") movie_id : String, @QueryMap optionsQuery: HashMap<String, Any>): JsonObject
//    https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=<<api_key>>&language=en-US

    @GET("${Constants.BASE_API}/movie/{movie_id}/reviews")
    suspend fun getListReview(@Path("movie_id") movie_id : String, @QueryMap optionsQuery: HashMap<String, Any>): CommentModelDTO
//    https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&language=en-US&page=1
}