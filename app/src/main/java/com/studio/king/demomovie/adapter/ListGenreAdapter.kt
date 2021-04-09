package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.GenreHomeHolder
import com.studio.king.demomovie.adapter.holder.MovieHomeHolder
import com.studio.king.demomovie.databinding.ViewItemGenreHomeHolderBinding
import com.studio.king.demomovie.databinding.ViewItemMovieHomeHolderBinding

class ListGenreAdapter : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        val binding: ViewItemGenreHomeHolderBinding = ViewItemGenreHomeHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreHomeHolder(binding)
    }
}

