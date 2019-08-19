package com.zwb.recyclerviewlibrary

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @ author : zhouweibin
 * @ time: 2019/8/15 20:00.
 * @ desc: recyclerView 帮助类
 **/
object RVUtil {
    /**
     * 是否反转
     */
    @JvmStatic
    fun isReverse(recyclerView: RecyclerView?): Boolean {
        var isReverse = false
        recyclerView?.layoutManager?.let {
            when (it) {
                is LinearLayoutManager -> isReverse = it.reverseLayout
                is GridLayoutManager -> isReverse = it.reverseLayout
                is StaggeredGridLayoutManager -> isReverse = it.reverseLayout
            }
        }
        return isReverse
    }

    /**
     * 是否垂直布局
     */
    @JvmStatic
    fun isVertical(recyclerView: RecyclerView?): Boolean {
        var isVertical = false
        recyclerView?.layoutManager?.let {
            when (it) {
                is LinearLayoutManager -> isVertical = it.orientation == LinearLayoutManager.VERTICAL
                is GridLayoutManager -> isVertical = it.orientation == GridLayoutManager.VERTICAL
                is StaggeredGridLayoutManager -> isVertical = it.orientation == StaggeredGridLayoutManager.VERTICAL
            }
        }
        return isVertical
    }

    /**
     * 第一个item是否可见
     */
    fun isFirstRowVisible(recyclerView: RecyclerView?): Boolean {
        var isFirstRowVisible = false
        recyclerView?.layoutManager?.let {
            when (it) {
                is LinearLayoutManager -> {
                    isFirstRowVisible = it.findFirstVisibleItemPosition() == 0
                }
                is GridLayoutManager -> isFirstRowVisible = it.findFirstVisibleItemPosition() == 0
                is StaggeredGridLayoutManager -> {
                    val pos = it.findFirstVisibleItemPositions(null)
                    if (pos.isNotEmpty()) {
                        val list = pos.toMutableList()
                        list.sort()
                        isFirstRowVisible = list[0] == 0
                    }
                }
            }
        }
        return isFirstRowVisible
    }

    /**
     * 最后一个item是否可见
     */
    fun isLastRowVisible(recyclerView: RecyclerView?): Boolean {
        var isLastRowVisible = false
        recyclerView?.layoutManager?.let {
            when (it) {
                is LinearLayoutManager -> isLastRowVisible = it.findLastVisibleItemPosition() == it.itemCount - 1
                is GridLayoutManager -> isLastRowVisible = it.findLastVisibleItemPosition() == it.itemCount - 1
                is StaggeredGridLayoutManager -> {
                    val pos = it.findLastVisibleItemPositions(null)
                    if (pos.isNotEmpty()) {
                        val list = pos.toMutableList()
                        list.sort()
                        isLastRowVisible = list[it.spanCount - 1] >= it.itemCount - 1
                    }
                }
            }
        }
        return isLastRowVisible
    }

    /**
     * 是否有数据
     */
    fun hasData(recyclerView: RecyclerView?): Boolean {
        var hasData = false
        recyclerView?.layoutManager?.let { hasData = it.itemCount > 0 }
        return hasData
    }

    /**
     * 获取滑动的距离(滚出屏幕的距离)
     */
    fun getScrollOffset(recyclerView: RecyclerView?, isVertical: Boolean = true): Int {
        var offset = 0
        recyclerView?.let {
            offset =
                if (isVertical) recyclerView.computeVerticalScrollOffset() else recyclerView.computeHorizontalScrollOffset()
        }
        return offset
    }

    /**
     * 获取整个列表的高度/宽度  包括屏幕内外的距离
     */
    fun getScrollRange(recyclerView: RecyclerView?, isVertical: Boolean = true): Int {
        var scrollRange = 0
        recyclerView?.let {
            scrollRange =
                if (isVertical) recyclerView.computeVerticalScrollRange() else recyclerView.computeHorizontalScrollRange()
        }
        return scrollRange
    }

    /**
     * 获取列表在屏幕内的大小(一般与宽高相等)
     */
    fun getScrollExtent(recyclerView: RecyclerView?, isVertical: Boolean = true): Int {
        var scrollExtent = 0
        recyclerView?.let {
            scrollExtent =
                if (isVertical) recyclerView.computeVerticalScrollExtent() else recyclerView.computeHorizontalScrollExtent()
        }
        return scrollExtent
    }

    /**
     * 获取宽/高
     */
    fun getSize(recyclerView: RecyclerView?, isHeight: Boolean = true): Int {
        var size = 0
        recyclerView?.let {
            size =
                if (isHeight) recyclerView.height else recyclerView.width
        }
        return size
    }
}