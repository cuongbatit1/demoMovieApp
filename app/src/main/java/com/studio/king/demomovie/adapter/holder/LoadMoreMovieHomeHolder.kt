package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemMovieLoadMoreHomeHolderBinding
import org.koin.core.component.KoinComponent

class LoadMoreMovieHomeHolder(itemView: ViewItemMovieLoadMoreHomeHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemMovieLoadMoreHomeHolderBinding = itemView
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {


    }
}