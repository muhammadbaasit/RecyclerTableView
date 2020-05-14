package com.pibm.tablenew.fixestable.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class SingleLineLinearLayout @JvmOverloads constructor(
        context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = 0
        var height = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val widthChild = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED)
            val heightChild = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED)
            childView.measure(widthChild, heightChild)
            width += childView.measuredWidth
            height = Math.max(height, childView.measuredHeight)
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var tempLeft = 0
        var tempHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val tempRight = tempLeft + childView.measuredWidth
            val tempT = 0
            var tempB = childView.measuredHeight
            if (tempHeight == 0) {
                tempHeight = tempB
            } else if (tempB != tempHeight) {
                tempB = tempHeight
            }
            childView.layout(tempLeft, tempT, tempRight, tempB)
            tempLeft += childView.measuredWidth
        }
    }
}