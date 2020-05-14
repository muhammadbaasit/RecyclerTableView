package com.pibm.tablenew.fixestable.widget

import android.graphics.Rect
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class TableLayoutManager : RecyclerView.LayoutManager() {
    private var verticalOffset = 0
    private var horizontalOffset = 0
    private var firstVisPos = 0
    private var lastVisPos = 0
    private val mItemAnchorMap = SparseArray<Rect>()
    private var oldChildCount = 1
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }
        if (childCount == 0 && state.isPreLayout) {
            return
        }
        if (childCount > 0 && state.didStructureChange()) {
            oldChildCount = childCount
            fill(recycler, state, 0)
            return
        } else if (childCount - oldChildCount > 0 && !state.didStructureChange()) {
            fill(recycler, state, 0)
            return
        }
        detachAndScrapAttachedViews(recycler)
        verticalOffset = 0
        firstVisPos = 0
        lastVisPos = state.itemCount
        fill(recycler, state, 0)
    }

    private fun fill(recycler: Recycler, state: RecyclerView.State, dy: Int): Int {
        var dy = dy
        var offsetTop = 0
        if (childCount > 0) {
            for (i in childCount - 1 downTo 0) {
                val child = getChildAt(i)
                if (dy > 0) {
                    if (getDecoratedBottom(child!!) < 0) {
                        removeAndRecycleView(child, recycler)
                        firstVisPos++
                    }
                } else if (dy < 0) {
                    if (getDecoratedTop(child!!) > height - paddingBottom) {
                        removeAndRecycleView(child, recycler)
                        lastVisPos--
                    }
                }
            }
        }
        if (dy >= 0) {
            var minPos = firstVisPos
            lastVisPos = itemCount - 1
            if (childCount > 0) {
                val lastView = getChildAt(childCount - 1)
                minPos = getPosition(lastView!!) + 1
                offsetTop = getDecoratedBottom(lastView)
            }
            for (i in minPos..lastVisPos) {
                val child = recycler.getViewForPosition(i)
                addView(child)
                measureChild(child, 0, 0)
                if (offsetTop - dy > height) {
                    removeAndRecycleView(child, recycler)
                    lastVisPos = i - 1
                } else {
                    val w = getDecoratedMeasuredWidth(child)
                    val h = getDecoratedMeasuredHeight(child)
                    var aRect = mItemAnchorMap[i]
                    if (aRect == null) {
                        aRect = Rect()
                    }
                    aRect[0, offsetTop + verticalOffset, w] = offsetTop + h + verticalOffset
                    mItemAnchorMap.put(i, aRect)
                    layoutDecorated(child, -horizontalOffset, offsetTop, -horizontalOffset + w,
                            offsetTop + h)
                    offsetTop += h
                }
            }
            val lastChild = getChildAt(childCount - 1)
            if (getPosition(lastChild!!) == itemCount - 1) {
                val gap = height - getDecoratedBottom(lastChild)
                if (gap > 0) {
                    dy -= gap
                }
            }
        } else {
            var maxPos = itemCount - 1
            firstVisPos = 0
            if (childCount > 0) {
                val firstView = getChildAt(0)
                maxPos = getPosition(firstView!!) - 1
            }
            for (i in maxPos downTo firstVisPos) {
                val aRect = mItemAnchorMap[i]
                if (aRect != null) {
                    if (aRect.bottom - verticalOffset - dy < 0) {
                        firstVisPos = i + 1
                        break
                    } else {
                        val child = recycler.getViewForPosition(i)
                        addView(child, 0)
                        measureChild(child, 0, 0)
                        layoutDecorated(child, aRect.left - horizontalOffset,
                                aRect.top - verticalOffset, aRect.right - horizontalOffset,
                                aRect.bottom - verticalOffset)
                    }
                }
            }
        }
        return dy
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
            dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        var dx = dx
        val firstView = getChildAt(0)
        val firstViewWidth = firstView!!.measuredWidth
        if (firstViewWidth <= width) {
            return 0
        }
        if (horizontalOffset + dx > firstViewWidth - width) {
            dx = 0
        } else if (horizontalOffset + dx <= 0) {
            dx = 0
        }
        horizontalOffset += dx
        offsetChildrenHorizontal(-dx)
        return dx
    }

    override fun scrollVerticallyBy(
            dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        if (dy == 0 || childCount == 0) {
            return 0
        }
        var realOffset = dy
        val firstView = getChildAt(0)
        val lastView = getChildAt(childCount - 1)

        //Optimize the case where the entire data set is too small to scroll
        val viewSpan = getDecoratedBottom(lastView!!) - getDecoratedTop(firstView!!)
        if (viewSpan < verticalSpace) {
            return 0
        }
        if (verticalOffset + realOffset < 0) {
            realOffset = -verticalOffset
        } else if (realOffset > 0) {
            if (getPosition(lastView) == itemCount - 1) {
                val gap = height - paddingBottom - getDecoratedBottom(lastView)
                realOffset = if (gap > 0) {
                    -gap
                } else if (gap == 0) {
                    0
                } else {
                    Math.min(realOffset, -gap)
                }
            }
        }
        realOffset = fill(recycler, state, realOffset)
        verticalOffset += realOffset
        offsetChildrenVertical(-realOffset)
        return realOffset
    }

    private val verticalSpace: Int
        private get() = height - paddingBottom - paddingTop

    private val horizontalSpace: Int
        private get() = width - paddingLeft - paddingRight

    init {
        isAutoMeasureEnabled = true
    }
}