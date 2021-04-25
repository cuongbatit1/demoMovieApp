package com.studio.king.demomovie.model

class ProductionModel(
    var id : String? = null,
    var logo_path : String? = null,
    var name : String? = null,
    var origin_country : String? = null
)

fun ProductionModel.getLogoPath() : String? {
    return logo_path?.let {
        "https://image.tmdb.org/t/p/original/${it}"
    }
}