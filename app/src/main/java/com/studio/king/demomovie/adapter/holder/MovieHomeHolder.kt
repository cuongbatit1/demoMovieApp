package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemMovieHomeHolderBinding
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.model.getPosterPath
import com.studio.king.demomovie.provider.PicassoProvider
import com.studio.king.demomovie.utils.setSingleClick
import org.koin.core.component.KoinComponent

class MovieHomeHolder(itemView: ViewItemMovieHomeHolderBinding, private val onActionItem: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemMovieHomeHolderBinding = itemView
    private var mMovieModel: MovieModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is MovieModel) {
            mMovieModel = t
            buildUITitle()
            buildUIImage()
            buildUIClickItem()
        }

    }

    private fun buildUITitle() {
        binding.textTitleMovieHome.text = mMovieModel?.title ?: ""
    }

    private fun buildUIImage() {
        if (mMovieModel?.getPosterPath() != null) {
            PicassoProvider.get().load(mMovieModel?.getPosterPath()).fit().placeholder(R.drawable.placeholder).error(R.drawable.image_error).into(binding.imageMovieHome)
        } else {
            binding.imageMovieHome.setImageResource(R.drawable.image_error)
        }
    }

    private fun buildUIClickItem() {
        binding.layoutImageMovieHome.setSingleClick {
            onActionItem.invoke(mMovieModel)
        }
    }
}