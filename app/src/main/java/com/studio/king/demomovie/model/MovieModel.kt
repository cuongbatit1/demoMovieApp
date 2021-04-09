package com.studio.king.demomovie.model

class MovieModel(
    var poster_path : String? = null,
    var adult : Boolean? = null,
    var backdrop_path : String? = null,
    var id : String? = null,
    var vote_count : Float? = null,
    var vote_average : Float? = null,
    var title : String? = null,
    var video : Boolean? = null,
    var release_date : String? = null,
    var genre_ids : MutableList<String>? = null,
    var original_language : String? = null,
    var overview : String? = null,
    var original_title : String? = null,
    var popularity : Float? = null,
    var media_type : String? = null
)

fun MovieModel.getPosterPath() : String? {
    return poster_path?.let {
        "https://image.tmdb.org/t/p/original/${it}"
    }
}

fun MovieModel.getBackdropPath() : String? {
    return backdrop_path?.let {
        "https://image.tmdb.org/t/p/original/${it}"
    }
}