package com.studio.king.demomovie.model

import com.studio.king.demomovie.utils.TypeDataHome

class LayoutCastModel(
        var layoutType : Int = 0,
        var typeData : TypeDataHome = TypeDataHome.TYPE_CAST,
        var listCast : MutableList<CastMovieModel>? = null,
        var currentPosition : Int = 0,
        var offsetScrollX : Int = 0,
)