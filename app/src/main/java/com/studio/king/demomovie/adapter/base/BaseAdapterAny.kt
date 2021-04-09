package com.studio.king.demomovie.adapter.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.studio.king.demomovie.databinding.ViewItemNoneHolderBinding

abstract class BaseAdapterAny : RecyclerView.Adapter<LifecycleViewHolder>() {
    var mList: MutableList<Any>? = null

    @CallSuper
    override fun onViewAttachedToWindow(holder: LifecycleViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: LifecycleViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }

    @CallSuper
    override fun onViewRecycled(holder: LifecycleViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun onBindViewHolder(holder: LifecycleViewHolder, position: Int) {
        if(position != RecyclerView.NO_POSITION){
            if (mList != null && mList!!.size > position) {
                holder.setData(mList!![position], position, this)
            } else {
                holder.setData(null, position, this)
            }
        }
    }


    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

//    class NoneHolder constructor(
//        itemView: ViewItemNoneHolderBinding
//    ) : LifecycleViewHolder(itemView.root) {
//
//        private val bindingNoneHolder : ViewItemNoneHolderBinding = itemView
//
//        override fun setData(
//            t: Any?,
//            position: Int,
//            adapter: BaseAdapterAny
//        ) {
//            val layoutParam = bindingNoneHolder.root.layoutParams
//            layoutParam.height = context.resources.getDimensionPixelOffset(R.dimen._120sdp)
//            itemView.layoutParams = layoutParam
//        }
//
//    }
//
    class NoneHolderViewPage constructor(
        itemView: ViewItemNoneHolderBinding
    ) : LifecycleViewHolder(itemView.root) {

        private val bindingNoneHolder : ViewItemNoneHolderBinding = itemView

        override fun setData(
            t: Any?,
            position: Int,
            adapter: BaseAdapterAny
        ) {

        }

    }
}