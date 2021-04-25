package com.studio.king.demomovie.model.dto

import com.studio.king.demomovie.model.CommentModel

class CommentModelDTO(
    var page : Int? = null,
    var results : MutableList<CommentModel>? = null,
    var total_pages : Int? = null,
    var total_results : Int? = null
)