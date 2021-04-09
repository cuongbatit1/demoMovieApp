package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.TrendingHomeHolder
import com.studio.king.demomovie.databinding.ViewItemTrendingHomeHolderBinding

class ListTrendingAdapter(private val onActionItem: (data: Any?) -> Unit) : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        val binding: ViewItemTrendingHomeHolderBinding = ViewItemTrendingHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingHomeHolder(binding, onActionItem)
    }
}

