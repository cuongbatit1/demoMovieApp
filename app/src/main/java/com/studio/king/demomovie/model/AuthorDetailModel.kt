package com.studio.king.demomovie.model

class AuthorDetailModel(
    var name : String? = null,
    var username : String? = null,
    var avatar_path : String? = null,
    var rating : Float? = null
)

fun AuthorDetailModel.getAvatarPath() : String? {
    return avatar_path?.let {
        "https://image.tmdb.org/t/p/original/${it}"
    }
}