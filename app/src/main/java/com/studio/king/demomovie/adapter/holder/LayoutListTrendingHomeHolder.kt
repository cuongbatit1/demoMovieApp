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
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent


class LayoutListTrendingHomeHolder(itemView: ViewItemRecyclerviewTrendingHolderBinding) : LifecycleViewHolder(
    itemView.root
),
    KoinComponent {

    private val binding: ViewItemRecyclerviewTrendingHolderBinding = itemView
    private var mLayoutUIModel: LayoutUIModel? = null

    private val horizontalLayoutManager by lazy { LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    ) }
//    private val mRecyclerView: RecyclerView by lazy {
//        binding.recyclerviewItemListGenre.apply {
//            setHasFixedSize(true)
//            addItemDecoration(object : RecyclerView.ItemDecoration() {
//                override fun getItemOffsets(
//                    outRect: Rect,
//                    view: View,
//                    parent: RecyclerView,
//                    state: RecyclerView.State
//                ) {
//                    val position = parent.getChildAdapterPosition(view) // item position
//                    when {
//                        position <= 0 -> {
//                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._16dp)
//                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._10dp)
//                        }
//                        position >= (parent.adapter?.itemCount!! - 1) -> {
//                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._10dp)
//                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._16dp)
//                        }
//                        else -> {
//                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._10dp)
//                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._10dp)
//                        }
//                    }
//                    outRect.bottom = 0
//                    outRect.top = 0
//                }
//
//            })
//            setRecycledViewPool(RecyclerView.RecycledViewPool())
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        val position = horizontalLayoutManager.findFirstVisibleItemPosition()
//                        mLayoutGenreUIModel?.currentPosition = position
//                        val firstItemView = horizontalLayoutManager.findViewByPosition(position)
//                        mLayoutGenreUIModel?.offsetScrollX = firstItemView?.left ?: 0
//                        LogUtil.e("onScrollStateChanged : $position - ${mLayoutGenreUIModel?.offsetScrollX}")
//                    }
//                }
//            })
//
//            val snapHelper: SnapHelper = StartSnapHelper()
//            snapHelper.attachToRecyclerView(this)
//        }
//    }

    private val mViewPager2: ViewPager2 by lazy {
        binding.ViewPager2ItemListTrending.apply {
            if (itemDecorationCount == 0) {
                offscreenPageLimit = 1
//                val offsetX =
//                        context.resources.getDimension(R.dimen._76dp) //viewpager_current_item_horizontal_margin
//
//                val pageTransformer =
//                        ViewPager2.PageTransformer { page: View, position: Float ->
//
//                            page.translationX = -offsetX * position
//
//                        }
//                setPageTransformer(pageTransformer)
//                setPageTransformer(MarginPageTransformer(context.resources.getDimension(R.dimen._20dp).toInt()))
//                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen._10dp)
//                val offsetPx = resources.getDimensionPixelOffset(R.dimen._20dp)
//                setPageTransformer { page, position ->
//                    val viewPager = page.parent.parent as ViewPager2
//                    val offset = position * -(2 * offsetPx + pageMarginPx)
//                    if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
//                        if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                            page.translationX = -offset
//                        } else {
//                            page.translationX = offset
//                        }
//                    } else {
//                        page.translationY = offset
//                    }
//                }
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                val total = context.resources.getDimensionPixelOffset(R.dimen._60dp)
                setPageTransformer { page, position ->
                    val viewPager = page.parent.parent as ViewPager2
//                    val offset = position * -(2 * offsetPx + pageMarginPx)
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
                        val all = context.resources.getDimensionPixelOffset(R.dimen._60dp)
                        when {
                            position == 0 -> {
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

    }

    private val mListTrendingAdapter: ListTrendingAdapter by lazy { ListTrendingAdapter() }

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
//        mRecyclerView.layoutManager = horizontalLayoutManager

        mViewPager2.adapter = mListTrendingAdapter
        if (mLayoutUIModel?.currentPosition != null && mLayoutUIModel?.currentPosition != 0) {
            mViewPager2.setCurrentItem(mLayoutUIModel?.currentPosition!!, false)
        }

        mListTrendingAdapter.mList = mLayoutUIModel?.listMovie as MutableList<Any>?
    }
}