package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.*
import com.studio.king.demomovie.databinding.*
import com.studio.king.demomovie.model.LayoutGenreUIModel
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.model.TitleModel
import com.studio.king.demomovie.utils.TypeLayoutHome
import com.studio.king.demomovie.utils.TypeLayoutMovie

class ListMovieAdapter(private val onActionItem: (data: Any?) -> Unit) : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        return when (viewType) {
            TypeLayoutMovie.TYPE_MOVIE -> {
                val binding: ViewItemMovieHomeHolderBinding = ViewItemMovieHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieHomeHolder(binding, onActionItem)
            }

            TypeLayoutMovie.TYPE_LOAD_MORE -> {
                val binding: ViewItemMovieLoadMoreHomeHolderBinding = ViewItemMovieLoadMoreHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreMovieHomeHolder(binding)
            }

            else -> {
                val binding: ViewItemNoneHolderBinding = ViewItemNoneHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NoneHolderViewPage(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = mList?.get(position)) {
            is MovieModel -> {
                TypeLayoutMovie.TYPE_MOVIE
            }
            is String -> {
                TypeLayoutMovie.TYPE_LOAD_MORE
            }
            else -> super.getItemViewType(position)
        }
    }
}

