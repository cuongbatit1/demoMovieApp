package com.studio.king.demomovie.model

class CastMovieModel(
    var gender : String? = null,
    var id : String? = null,
    var known_for_department : String? = null,
    var name : String? = null,
    var original_name : String? = null,
    var popularity : String? = null,
    var profile_path : String? = null,
    var cast_id : String? = null,
    var character : String? = null,
    var credit_id : String? = null,
    var order : String? = null,
)

fun CastMovieModel.getProfilePath() : String? {
    return profile_path?.let {
        "https://image.tmdb.org/t/p/original/${it}"
    }
}
