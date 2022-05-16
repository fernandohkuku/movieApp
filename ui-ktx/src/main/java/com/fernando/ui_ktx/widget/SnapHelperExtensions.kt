package com.fernando.ui_ktx.widget

import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.fernando.ui_ktx.R
import com.google.android.material.card.MaterialCardView


fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}


interface OnSnapPositionChangeListener {
    fun onSnapPositionChange(position: Int)
}

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null,
    var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL,
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val view = snapHelper.findSnapView(recyclerView.layoutManager)
        val position = recyclerView.layoutManager?.getPosition(view!!)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position!!)

        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            viewHolder?.itemView?.animate()?.setDuration(350)?.scaleX(0.7F)?.scaleY(0.7F)
                ?.setInterpolator(AccelerateInterpolator())?.start()
            maybeNotifySnapPositionChange(recyclerView)
        }else{
            viewHolder?.itemView?.animate()?.setDuration(350)?.scaleX(1F)?.scaleY(1F)
                ?.setInterpolator(AccelerateInterpolator())?.start()

        }

    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
            this.snapPosition = snapPosition
        }
    }
}