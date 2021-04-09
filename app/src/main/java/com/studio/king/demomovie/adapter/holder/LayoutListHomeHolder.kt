package com.studio.king.demomovie.adapter.holder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.ListMovieAdapter
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.custom.StartSnapHelper
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewHolderBinding
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent

class LayoutListHomeHolder(itemView: ViewItemRecyclerviewHolderBinding, private val onActionItem: (data: Any?) -> Unit, private val onActionLoadMore: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemRecyclerviewHolderBinding = itemView
    private var mLayoutUIModel: LayoutUIModel? = null

    private val horizontalLayoutManager by lazy { LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
    ) }
    private val mRecyclerView: RecyclerView by lazy {
        binding.recyclerviewItemList.apply {
            setHasFixedSize(true)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view) // item position
                    when {
                        position <= 0 -> {
                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._16dp)
                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._10dp)
                        }
                        position >= (parent.adapter?.itemCount!! - 1) -> {
                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._10dp)
                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._16dp)
                        }
                        else -> {
                            outRect.left = context.resources.getDimensionPixelOffset(R.dimen._10dp)
                            outRect.right = context.resources.getDimensionPixelOffset(R.dimen._10dp)
                        }
                    }
                    outRect.bottom = 0
                    outRect.top = 0
                }

            })
            setRecycledViewPool(RecyclerView.RecycledViewPool())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = horizontalLayoutManager.findFirstVisibleItemPosition()
                        mLayoutUIModel?.currentPosition = position
                        val firstItemView = horizontalLayoutManager.findViewByPosition(position)
                        mLayoutUIModel?.offsetScrollX = firstItemView?.left ?: 0
                        LogUtil.e("onScrollStateChanged : $position - ${mLayoutUIModel?.offsetScrollX}")
                        val totalItemCount = horizontalLayoutManager.itemCount
                        val findLastCompletelyVisible = horizontalLayoutManager.findLastCompletelyVisibleItemPosition()
                        if (totalItemCount == findLastCompletelyVisible + 1) {
                            onActionLoadMore.invoke(mLayoutUIModel)
                        }
                    }
                }
            })
            val snapHelper: SnapHelper = StartSnapHelper()
            snapHelper.attachToRecyclerView(this)

        }
    }

    private val mListMovieAdapter: ListMovieAdapter by lazy { ListMovieAdapter(onActionItem) }

    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is LayoutUIModel) {
            mLayoutUIModel = t
            buildUIRecyclerView()
        }
    }

    private fun buildUIRecyclerView() {
        mRecyclerView.layoutManager = horizontalLayoutManager
        mRecyclerView.adapter = mListMovieAdapter
        if (mLayoutUIModel?.isScrollEnd != null && mLayoutUIModel!!.isScrollEnd) {
            mLayoutUIModel?.listMovie?.size?.let {
                if (it > 0) {
                    horizontalLayoutManager.scrollToPosition(it - 1)
                }
            }

        } else {
            if (mLayoutUIModel?.currentPosition != null && mLayoutUIModel?.currentPosition != 0) {
                horizontalLayoutManager.scrollToPositionWithOffset(mLayoutUIModel!!.currentPosition, mLayoutUIModel?.offsetScrollX ?: 0)
            }
        }

        mListMovieAdapter.mList = mLayoutUIModel?.listMovie as MutableList<Any>?
    }
}