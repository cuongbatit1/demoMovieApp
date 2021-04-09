package com.studio.king.demomovie.adapter.holder

import android.graphics.Color
import android.view.View
import com.squareup.picasso.Picasso
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.databinding.ViewItemTitleHolderBinding
import com.studio.king.demomovie.model.TitleModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TitleHomeHolder(itemView: ViewItemTitleHolderBinding) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemTitleHolderBinding = itemView
    private var mTitleModel: TitleModel? = null
    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is TitleModel) {
            mTitleModel = t
            buildUITitle()
            buildUIIcon()
            buildUIBackGround()
        }

    }

    private fun buildUITitle() {
        binding.textTitleHolder.text = mTitleModel?.title ?: ""
    }

    private fun buildUIIcon() {
        if (mTitleModel?.isIcon != null && mTitleModel!!.isIcon) {
            binding.iconTitleHolder.visibility = View.VISIBLE
        } else {
            binding.iconTitleHolder.visibility = View.GONE
        }
    }
    private fun buildUIBackGround() {
        if (mTitleModel?.isBackground != null && mTitleModel!!.isBackground) {
            binding.root.setBackgroundColor(Color.parseColor("#F8F8F8"))
        } else {
            binding.root.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}