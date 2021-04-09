package com.studio.king.demomovie.adapter.holder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.ListGenreAdapter
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.custom.StartSnapHelper
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewGenreHolderBinding
import com.studio.king.demomovie.model.LayoutGenreUIModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent


class LayoutListGenreHomeHolder(itemView: ViewItemRecyclerviewGenreHolderBinding, private val onActionItem: (data: Any?) -> Unit) : LifecycleViewHolder(
    itemView.root
),
    KoinComponent {

    private val binding: ViewItemRecyclerviewGenreHolderBinding = itemView
    private var mLayoutGenreUIModel: LayoutGenreUIModel? = null

    private val horizontalLayoutManager by lazy { LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    ) }
    private val mRecyclerView: RecyclerView by lazy {
        binding.recyclerviewItemListGenre.apply {
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
                        mLayoutGenreUIModel?.currentPosition = position
                        val firstItemView = horizontalLayoutManager.findViewByPosition(position)
                        mLayoutGenreUIModel?.offsetScrollX = firstItemView?.left ?: 0
                        LogUtil.e("onScrollStateChanged : $position - ${mLayoutGenreUIModel?.offsetScrollX}")
                    }
                }
            })

            val snapHelper: SnapHelper = StartSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private val mListGenreAdapter: ListGenreAdapter by lazy { ListGenreAdapter(onActionItem) }

    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is LayoutGenreUIModel) {
            mLayoutGenreUIModel = t
            buildUIRecyclerView()
        }
    }

    private fun buildUIRecyclerView() {
        mRecyclerView.layoutManager = horizontalLayoutManager

        mRecyclerView.adapter = mListGenreAdapter
        if (mLayoutGenreUIModel?.currentPosition != null && mLayoutGenreUIModel?.currentPosition != 0) {
            horizontalLayoutManager.scrollToPositionWithOffset(
                mLayoutGenreUIModel!!.currentPosition,
                mLayoutGenreUIModel?.offsetScrollX ?: 0
            )
        }

        mListGenreAdapter.mList = mLayoutGenreUIModel?.listGenres as MutableList<Any>?
    }


}