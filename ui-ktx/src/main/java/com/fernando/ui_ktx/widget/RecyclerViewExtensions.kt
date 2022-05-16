package com.fernando.ui_ktx.widget

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

fun <T : Any> asyncListDiffer(
    areContentsTheSame: (T, T) -> Boolean,
    areItemsTheSame: (T, T) -> Boolean
) =
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(@NonNull oldItem: T, @NonNull newItem: T): Boolean {
            return areContentsTheSame(oldItem, newItem)
        }
    }


fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener = SnapOnScrollListener(snapHelper, onSnapPositionChangeListener, behavior)
    addOnScrollListener(snapOnScrollListener)
}

fun RecyclerView.onScrollListener(onScrollChanged:(state:Int)-> Unit) = object : RecyclerView.OnScrollListener(){
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        onScrollChanged(newState)
    }
}


fun RecyclerView.findViewHolder(position:Int?): RecyclerView.ViewHolder? {
    if (position !=null){
        return findViewHolderForAdapterPosition(position)
    }
    return null
}


fun RecyclerView.animateScroll(state:Int) {
    if (state == RecyclerView.SCROLL_STATE_IDLE){

    }
}