package com.studio.king.demomovie.adapter.holder

import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemCastHolderBinding
import com.studio.king.demomovie.databinding.ViewItemMovieHomeHolderBinding
import com.studio.king.demomovie.model.CastMovieModel
import com.studio.king.demomovie.model.MovieModel
import com.studio.king.demomovie.model.getPosterPath
import com.studio.king.demomovie.model.getProfilePath
import com.studio.king.demomovie.provider.PicassoProvider
import com.studio.king.demomovie.utils.setSingleClick
import org.koin.core.component.KoinComponent

class CastDetailHolder(itemView: ViewItemCastHolderBinding, private val onActionItem: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemCastHolderBinding = itemView
    private var mCastMovieModel: CastMovieModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is CastMovieModel) {
            mCastMovieModel = t
            buildUITitle()
            buildUIImage()
            buildUIClickItem()
        }

    }

    private fun buildUITitle() {
        binding.textTitleCastMovie.text = mCastMovieModel?.name ?: ""
        binding.textCastCastMovie.text = mCastMovieModel?.character ?: ""
    }

    private fun buildUIImage() {
        if (mCastMovieModel?.getProfilePath() != null) {
            PicassoProvider.get().load(mCastMovieModel?.getProfilePath()).fit().placeholder(R.drawable.placeholder).error(R.drawable.image_error).into(binding.imageCastMovie)
        } else {
            binding.imageCastMovie.setImageResource(R.drawable.image_error)
        }
    }

    private fun buildUIClickItem() {
        binding.layoutImageCastMovie.setSingleClick {
            onActionItem.invoke(mCastMovieModel)
        }
    }
}