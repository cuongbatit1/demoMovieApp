package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemMovieLoadMoreHomeHolderBinding
import com.studio.king.demomovie.databinding.ViewItemMovieLoadMoreViewPagerHolderBinding
import org.koin.core.component.KoinComponent

class LoadMoreViewPagerHolder(itemView: ViewItemMovieLoadMoreViewPagerHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemMovieLoadMoreViewPagerHolderBinding = itemView
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {


    }
}