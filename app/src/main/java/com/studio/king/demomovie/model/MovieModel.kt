package com.studio.king.demomovie.model

data class MovieModel(
    var poster_path : String? = null,
    var adult : Boolean? = null,
    var backdrop_path : String? = null,
    var budget : Float? = null,
    var genres : MutableList<GenresModel>? = null,
    var homepage : String? = null,
    var id : String? = null,
    var imdb_id : String? = null,

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
    var media_type : String? = null,
    var production_companies : MutableList<ProductionModel>? = null,
    var production_countries : MutableList<ProductionContriesModel>? = null,
    var revenue : Float? = null,
    var runtime : Float? = null,
    var spoken_languages : MutableList<SpokenLanguagesModel>? = null,
    var status : String? = null,
    var tagline : String? = null,

    var description : String? = null,
    var favorite_count : Float? = null,
    var item_count : Int? = null,
    var iso_639_1 : String? = null,
    var list_type : String? = null,
    var name : String? = null,
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

