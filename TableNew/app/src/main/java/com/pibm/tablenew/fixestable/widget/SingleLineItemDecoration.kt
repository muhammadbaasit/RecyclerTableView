package com.pibm.tablenew.fixestable.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SingleLineItemDecoration(divider_height: Int, divider_color: Int) : ItemDecoration() {
    var lineHeight = 4
        private set
    var lineColor = Color.BLACK
        private set
    private val paint: Paint

    fun setLineHeight(lineHeight: Int): SingleLineItemDecoration {
        this.lineHeight = lineHeight
        return this
    }

    fun setLineColor(lineColor: Int): SingleLineItemDecoration {
        this.lineColor = lineColor
        paint.color = lineColor
        return this
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val bottom = child.bottom
            c.drawRect(left.toFloat(), bottom.toFloat(), right.toFloat(), bottom + lineHeight.toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = lineHeight
    }

    init {
        lineHeight = divider_height
        lineColor = divider_color
        paint = Paint()
        paint.color = lineColor
        paint.alpha = 240
    }
}