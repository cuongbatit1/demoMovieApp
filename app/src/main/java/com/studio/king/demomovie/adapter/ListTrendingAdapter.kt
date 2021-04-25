package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.LoadMoreMovieHomeHolder
import com.studio.king.demomovie.adapter.holder.LoadMoreViewPagerHolder
import com.studio.king.demomovie.adapter.holder.MovieHomeHolder
import com.studio.king.demomovie.adapter.holder.TrendingHomeHolder
import com.studio.king.demomovie.databinding.*
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.utils.TypeLayoutMovie

class ListTrendingAdapter(private val onActionItem: (data: Any?) -> Unit) : BaseAdapterAny() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
//        val binding: ViewItemTrendingHomeHolderBinding = ViewItemTrendingHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TrendingHomeHolder(binding, onActionItem)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        return when (viewType) {
            TypeLayoutMovie.TYPE_MOVIE -> {
                val binding: ViewItemTrendingHomeHolderBinding = ViewItemTrendingHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TrendingHomeHolder(binding, onActionItem)
            }

            TypeLayoutMovie.TYPE_LOAD_MORE -> {
                val binding: ViewItemMovieLoadMoreViewPagerHolderBinding = ViewItemMovieLoadMoreViewPagerHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewPagerHolder(binding)
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

