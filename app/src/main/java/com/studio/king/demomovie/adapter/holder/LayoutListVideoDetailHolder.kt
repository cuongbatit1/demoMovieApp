package com.studio.king.demomovie.adapter.holder

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.ListTrendingAdapter
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.base.ViewPager2PageChangeCallback
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewTrendingHolderBinding
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewVideoDetailHolderBinding
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent


class LayoutListVideoDetailHolder(itemView: ViewItemRecyclerviewVideoDetailHolderBinding, private val onActionItem: (data: Any?) -> Unit, private val onActionLoadMore: (data: Any?) -> Unit) : LifecycleViewHolder(
    itemView.root
),
    KoinComponent {

    private val binding: ViewItemRecyclerviewVideoDetailHolderBinding = itemView
    private var mLayoutUIModel: LayoutUIModel? = null

    private val mViewPager2: ViewPager2 by lazy {
        binding.ViewPager2ItemVideoDetail.apply {
            if (itemDecorationCount == 0) {
                offscreenPageLimit = 1
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                val total = context.resources.getDimensionPixelOffset(R.dimen._160dp)
                setPageTransformer { page, position ->
                    val viewPager = page.parent.parent as ViewPager2
                    val offset = position * -total
                    if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                        if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                            page.translationX = -offset
                        } else {
                            page.translationX = offset
                        }
                    } else {
                        page.translationY = offset
                    }
                }
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                    ) {

                        val position = parent.getChildAdapterPosition(view)
                        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                        val all = context.resources.getDimensionPixelOffset(R.dimen._160dp)
                        when (position) {
                            0 -> {
                                outRect.left = offsetPx
                                outRect.right = all - offsetPx
                            }
                            else -> {
                                outRect.left = offsetPx  + pageMarginPx
                                outRect.right =
                                    all - offsetPx
                            }
                        }
                    }
                })
                registerOnPageChangeCallback(viewPager2PageChangeCallback)
            }
        }
    }

    private val viewPager2PageChangeCallback = ViewPager2PageChangeCallback {
        LogUtil.d("viewPager2PageChangeCallback: $it")
        mLayoutUIModel?.currentPosition = it
        if (mLayoutUIModel?.listMovie?.size != null && mLayoutUIModel?.currentPosition != null && mLayoutUIModel?.currentPosition!! >= (mLayoutUIModel?.listMovie?.size!! - 1)) {
            onActionLoadMore.invoke(mLayoutUIModel)
        }


    }

    private val mListTrendingAdapter: ListTrendingAdapter by lazy { ListTrendingAdapter(onActionItem) }

    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is LayoutUIModel) {
            mLayoutUIModel = t
            buildUIViewPager2()
        }
    }

    private fun buildUIViewPager2() {
        mViewPager2.adapter = mListTrendingAdapter
        mListTrendingAdapter.mList = mLayoutUIModel?.listMovie as MutableList<Any>?

        if (mLayoutUIModel?.isScrollEnd != null && mLayoutUIModel!!.isScrollEnd) {
            mLayoutUIModel?.listMovie?.size?.let {
                if (it > 0) {
                    mViewPager2.setCurrentItem(it - 1, false)
                }
            }

        } else {
            if (mLayoutUIModel?.currentPosition != null && mLayoutUIModel?.currentPosition != 0) {
                mViewPager2.setCurrentItem(mLayoutUIModel?.currentPosition!!, false)
            }
        }

    }
}