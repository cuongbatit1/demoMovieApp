package com.studio.king.demomovie.adapter.holder.detail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.studio.king.demomovie.R
import com.studio.king.demomovie.adapter.ListCastAdapter
import com.studio.king.demomovie.adapter.ListMovieAdapter
import com.studio.king.demomovie.adapter.base.BaseAdapterAny
import com.studio.king.demomovie.adapter.base.LifecycleViewHolder
import com.studio.king.demomovie.custom.StartSnapHelper
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewCastHolderBinding
import com.studio.king.demomovie.databinding.ViewItemRecyclerviewHolderBinding
import com.studio.king.demomovie.model.LayoutCastModel
import com.studio.king.demomovie.model.LayoutUIModel
import com.studio.king.demomovie.utils.LogUtil
import org.koin.core.component.KoinComponent

class LayoutCastHolder(itemView: ViewItemRecyclerviewCastHolderBinding, private val onActionItem: (data: Any?) -> Unit) : LifecycleViewHolder(itemView.root),
    KoinComponent {

    private val binding: ViewItemRecyclerviewCastHolderBinding = itemView
    private var mLayoutCastModel: LayoutCastModel? = null

    private val horizontalLayoutManager by lazy { LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
    ) }
    private val mRecyclerView: RecyclerView by lazy {
        binding.recyclerviewItemListCast.apply {
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
                        mLayoutCastModel?.currentPosition = position
                        val firstItemView = horizontalLayoutManager.findViewByPosition(position)
                        mLayoutCastModel?.offsetScrollX = firstItemView?.left ?: 0
                        LogUtil.e("onScrollStateChanged : $position - ${mLayoutCastModel?.offsetScrollX}")
                        val totalItemCount = horizontalLayoutManager.itemCount
                        val findLastCompletelyVisible = horizontalLayoutManager.findLastCompletelyVisibleItemPosition()
                        if (totalItemCount == findLastCompletelyVisible + 1) {
//                            onActionLoadMore.invoke(mLayoutCastModel)
                        }
                    }
                }
            })
            val snapHelper: SnapHelper = StartSnapHelper()
            snapHelper.attachToRecyclerView(this)

        }
    }

    private val mListMovieAdapter: ListCastAdapter by lazy { ListCastAdapter(onActionItem) }

    override fun setData(
        t: Any?,
        position: Int,
        adapter: BaseAdapterAny
    ) {
        if (t is LayoutCastModel) {
            mLayoutCastModel = t
            buildUIRecyclerView()
        }
    }

    private fun buildUIRecyclerView() {
        mRecyclerView.layoutManager = horizontalLayoutManager
        mRecyclerView.adapter = mListMovieAdapter

            if (mLayoutCastModel?.currentPosition != null && mLayoutCastModel?.currentPosition != 0) {
                horizontalLayoutManager.scrollToPositionWithOffset(mLayoutCastModel!!.currentPosition, mLayoutCastModel?.offsetScrollX ?: 0)
            }


        mListMovieAdapter.mList = mLayoutCastModel?.listCast as MutableList<Any>?
    }
}