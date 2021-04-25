package com.studio.king.demomovie.adapter.holder.detail

import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemRateHolderBinding
import com.studio.king.demomovie.databinding.ViewItemWriteCommentHolderBinding
import com.studio.king.demomovie.model.WriteCommentDetailModel
import com.studio.king.demomovie.utils.setSingleClick
import org.koin.core.component.KoinComponent

class ComentReviewHolder(itemView: ViewItemWriteCommentHolderBinding, private val onActionComment: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemWriteCommentHolderBinding = itemView
    private var mWriteCommentDetailModel: WriteCommentDetailModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is WriteCommentDetailModel) {
            mWriteCommentDetailModel = t
            buildUIRatingBar()
            buildUITextRate()
            buildUIBtnWriteAComment()
        }
    }

    private fun buildUIRatingBar() {
        binding.ratingBarItemWriteComment.rating = mWriteCommentDetailModel?.star ?: 0f
    }

    private fun buildUITextRate() {
        binding.textRateItemWriteComment.text = "${mWriteCommentDetailModel?.star ?: "0.0"}"

    }

    private fun buildUIBtnWriteAComment() {
        binding.btnWriteAComment.setSingleClick(onActionComment)
    }
}