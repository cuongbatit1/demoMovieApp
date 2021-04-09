package com.studio.king.demomovie.model

import com.studio.king.demomovie.utils.TypeDataHome

class LayoutUIModel(
        var layoutType : Int = 0,
        var typeData : TypeDataHome = TypeDataHome.TYPE_POPULAR,
        var listMovie : MutableList<Any>? = null,
        var page : Int? = null,
        var totalPage : Int? = null,
        var currentPosition : Int = 0,
        var offsetScrollX : Int = 0,
        var isScrollEnd : Boolean = false
)