package com.studio.king.demomovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.adapter.holder.TitleHomeHolder
import com.studio.king.demomovie.adapter.holder.detail.RateDetailHolder
import com.studio.king.demomovie.adapter.holder.detail.TitleDescriptionDetailHolder
import com.studio.king.demomovie.adapter.holder.detail.WriteAComentDetailHolder
import com.studio.king.demomovie.databinding.*
import com.studio.king.demomovie.model.*
import com.studio.king.demomovie.utils.TypeLayoutDetailMovie

class DetailMovieAdapter(private val onActionItem: (data: Any?) -> Unit, private val onActionLoadMore: (data: Any?) -> Unit,
    private val onActionReadMore: (data: Any?) -> Unit) : BaseAdapterAny() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {
        return when (viewType) {
            TypeLayoutDetailMovie.TYPE_TITLE -> {
                val binding: ViewItemTitleHolderBinding = ViewItemTitleHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleHomeHolder(binding)
            }

            TypeLayoutDetailMovie.TYPE_RATE -> {
                val binding: ViewItemRateHolderBinding = ViewItemRateHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RateDetailHolder(binding)
            }

            TypeLayoutDetailMovie.TYPE_TITLE_DETAIL -> {
                val binding: ViewItemTitleDetailHolderBinding = ViewItemTitleDetailHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleDescriptionDetailHolder(binding, onActionReadMore)
            }
            TypeLayoutDetailMovie.TYPE_WRITE_A_COMMENT -> {
                val binding: ViewItemWriteCommentHolderBinding = ViewItemWriteCommentHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WriteAComentDetailHolder(binding, onActionReadMore)
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
                TypeLayoutDetailMovie.TYPE_TITLE
            }
            is RateDetailModel -> {
                TypeLayoutDetailMovie.TYPE_RATE
            }
            is TitleDetailModel -> {
                TypeLayoutDetailMovie.TYPE_TITLE_DETAIL
            }
            is WriteCommentDetailModel -> {
                TypeLayoutDetailMovie.TYPE_WRITE_A_COMMENT
            }

            else -> super.getItemViewType(position)
        }
    }
}