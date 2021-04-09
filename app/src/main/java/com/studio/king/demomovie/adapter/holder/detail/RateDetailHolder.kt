package com.studio.king.demomovie.adapter.holder.detail

import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemRateHolderBinding
import com.studio.king.demomovie.model.RateDetailModel
import org.koin.core.component.KoinComponent

class RateDetailHolder(itemView: ViewItemRateHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemRateHolderBinding = itemView
    private var mRateDetailModel: RateDetailModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is RateDetailModel) {
            mRateDetailModel = t
            buildUITextRatingBar()
            buildUIRatingBar()
            buildUITextTime()
        }

    }

    private fun buildUITextRatingBar() {
        binding.textRateItemRate.text = "${mRateDetailModel?.star ?: ""}"
    }

    private fun buildUIRatingBar() {
        binding.ratingBarItemRate.rating = mRateDetailModel?.star ?: 0f
    }

    private fun buildUITextTime() {
        binding.textTimeItemRate.text = mRateDetailModel?.textTime ?: ""
    }
}