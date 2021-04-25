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

class ListCastAdapter(private val onActionItem: (data: Any?) -> Unit) : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        val binding: ViewItemCastHolderBinding = ViewItemCastHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastDetailHolder(binding, onActionItem)
    }


}

