package com.noah.scorereporter.pages.team

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SeasonItemDecoration(private val spanCount: Int = 4) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        outRect.left = 10
        outRect.top = 10
        outRect.bottom = 0
        if (position % spanCount < (spanCount - 1)) {
            outRect.right = 10
        } else {
            outRect.right = 0
        }
    }
}