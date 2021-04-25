package com.studio.king.demomovie.model

class CommentModel(
    var author : String? = null,
    var content : String? = null,
    var created_at : String? = null,
    var id : String? = null,
    var updated_at : String? = null,
    var url : String? = null,
    var author_details : MutableList<AuthorDetailModel>? = null
)