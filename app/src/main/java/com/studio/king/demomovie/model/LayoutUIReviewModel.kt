package com.studio.king.demomovie.model


class LayoutUIReviewModel(
        var listReview : MutableList<Any>? = null,
        var page : Int? = null,
        var totalPage : Int? = null,
        var currentPosition : Int = 0,
        var offsetScrollX : Int = 0,
        var isScrollEnd : Boolean = false
)