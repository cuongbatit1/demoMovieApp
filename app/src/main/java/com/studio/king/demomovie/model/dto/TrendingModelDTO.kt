package com.studio.king.demomovie.model.dto

import com.studio.king.demomovie.model.MovieModel

class TrendingModelDTO(
    var page : Int? = null,
    var results : MutableList<MovieModel>? = null,
    var total_pages : Int? = null,
    var total_results : Int? = null
)