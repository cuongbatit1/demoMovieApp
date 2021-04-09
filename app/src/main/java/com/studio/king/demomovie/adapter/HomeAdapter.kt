package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.LayoutListGenreHomeHolder
import com.studio.king.demomovie.adapter.holder.LayoutListHomeHolder
import com.studio.king.demomovie.adapter.holder.LayoutListTrendingHomeHolder
import com.studio.king.demomovie.adapter.holder.TitleHomeHolder
import com.studio.king.demomovie.databinding.*
import com.studio.king.demomovie.model.LayoutGenreUIModel
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.model.TitleModel
import com.studio.king.demomovie.utils.TypeLayoutHome

class HomeAdapter(private val onActionItem: (data: Any?) -> Unit, private val onActionLoadMore: (data: Any?) -> Unit) : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        return when (viewType) {
            TypeLayoutHome.TYPE_TITLE -> {
                val binding: ViewItemTitleHolderBinding = ViewItemTitleHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleHomeHolder(binding)
            }

            TypeLayoutHome.TYPE_LIST_MOVIE -> {
                val binding: ViewItemRecyclerviewHolderBinding = ViewItemRecyclerviewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LayoutListHomeHolder(binding, onActionItem, onActionLoadMore)
            }

            TypeLayoutHome.TYPE_LIST_GENRE -> {
                val binding: ViewItemRecyclerviewGenreHolderBinding = ViewItemRecyclerviewGenreHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LayoutListGenreHomeHolder(binding)
            }
            TypeLayoutHome.TYPE_LIST_TRENDING -> {
                val binding: ViewItemRecyclerviewTrendingHolderBinding = ViewItemRecyclerviewTrendingHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LayoutListTrendingHomeHolder(binding)
            }

            else -> {
                val binding: ViewItemNoneHolderBinding = ViewItemNoneHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NoneHolderViewPage(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = mList?.get(position)) {
            is TitleModel -> {
                TypeLayoutHome.TYPE_TITLE
            }
            is LayoutUIModel -> {
                if (item.layoutType == 0) {
                    TypeLayoutHome.TYPE_LIST_MOVIE
                } else {
                    TypeLayoutHome.TYPE_LIST_TRENDING
                }
            }
            is LayoutGenreUIModel -> {
                TypeLayoutHome.TYPE_LIST_GENRE
            }
            else -> super.getItemViewType(position)
        }
    }
}