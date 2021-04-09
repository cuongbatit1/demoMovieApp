package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemTrendingHomeHolderBinding
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.model.getBackdropPath
import com.studio.king.demomovie.model.getPosterPath
import com.studio.king.demomovie.provider.PicassoProvider
import org.koin.core.component.KoinComponent

class TrendingHomeHolder(itemView: ViewItemTrendingHomeHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemTrendingHomeHolderBinding = itemView
    private var mMovieModel: MovieModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is MovieModel) {
            mMovieModel = t
            buildUIImage()
        }

    }

    private fun buildUIImage() {
        if (mMovieModel?.getPosterPath() != null) {
            PicassoProvider.get().load(mMovieModel?.getBackdropPath()).fit().placeholder(R.drawable.placeholder).error(R.drawable.image_error).into(binding.imageTrendingHome)
        } else {
            binding.imageTrendingHome.setImageResource(R.drawable.image_error)
        }
    }
}