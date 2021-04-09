package com.studio.king.demomovie.adapter.holder.detail

import android.view.View
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemRateHolderBinding
import com.studio.king.demomovie.databinding.ViewItemTitleDetailHolderBinding
import com.studio.king.demomovie.model.TitleDetailModel
import com.studio.king.demomovie.utils.setSingleClick
import org.koin.core.component.KoinComponent

class TitleDescriptionDetailHolder(itemView: ViewItemTitleDetailHolderBinding, private val onActionReadMore: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemTitleDetailHolderBinding = itemView
    private var mTitleDetailModel: TitleDetailModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is TitleDetailModel) {
            mTitleDetailModel = t
            buildUITitle()
            buildUIDescription()
            buildUIBtnReadMore()
        }

    }

    private fun buildUITitle() {
        binding.textTitleItemTitleDetail.text = mTitleDetailModel?.title ?: ""
    }

    private fun buildUIDescription() {
        binding.textDetailItemTitleDetail.text = mTitleDetailModel?.description ?: ""
//        if (mTitleDetailModel?.isShowReadMore != null && mTitleDetailModel!!.isShowReadMore) {
//            binding.textDetailItemTitleDetail.maxLines = 5
//        } else {
//            binding.textDetailItemTitleDetail.maxLines = -1
//        }
    }

    private fun buildUIBtnReadMore() {
        if (mTitleDetailModel?.isShowReadMore != null && mTitleDetailModel!!.isShowReadMore) {
            binding.textReadMoreItemTitleDetail.visibility = View.VISIBLE
            binding.textReadMoreItemTitleDetail.setSingleClick {
                onActionReadMore.invoke(mTitleDetailModel)
            }
        } else {
            binding.textReadMoreItemTitleDetail.visibility = View.GONE
        }

    }
}