package com.zwb.recyclerviewlibrary

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.Exception

/**
 * @ author : zhouweibin
 * @ time: 2019/8/15 17:01.
 * @ desc: 头尾分割线 用来占位
 **/
class HFDecoration(private var isHeader: Boolean = true) : RecyclerView.ItemDecoration() {
    private var mHeight = 0
    private var mWidth = 0
    /**
     * 设置分割线大小
     */
    fun setSize(mHeight: Int, mWidth: Int) {
        this.mHeight = mHeight
        this.mWidth = mWidth
    }


    override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, recyclerView, state)
        try {
            setOutRect(outRect, view, recyclerView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOutRect(outRect: Rect, view: View, recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            // 是否需要分割线
            var needSpace = false
            if (it is GridLayoutManager) {
                val adapterPosition = recyclerView.getChildAdapterPosition(view)
                // 示例：spanCount = 6  spanSize = 2  spanIndex 0,2,4  groupIndex:第几组
                val spanCount = it.spanCount
                val spanSizeLookup = it.spanSizeLookup
                val groupIndex = spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount)
                val lastGroupIndex = spanSizeLookup.getSpanGroupIndex(it.itemCount - 1, spanCount)
                // 头部 且是第一组
                if (isHeader && groupIndex == 0) {
                    needSpace = true
                } else if (!isHeader && groupIndex == lastGroupIndex) { // 尾部，且是最后一组
                    needSpace = true
                }
            } else {
                val position = recyclerView.getChildLayoutPosition(view)
                Log.e("zwb", "position === $position")
                var spanCount = 1
                if (it is StaggeredGridLayoutManager) {
                    spanCount = it.spanCount
                }
                if (isHeader) {
                    needSpace = position < spanCount
                } else {
                    val count = it.itemCount
                    needSpace = position >= count - spanCount
                    // TODO StaggeredGridLayoutManager 需要检测
                }
                Log.e(
                    "zwb",
                    "isHeader = $isHeader needSpace === $needSpace    position = $position  count = ${it.itemCount}  spanCount = $spanCount"
                )
            }
            if (needSpace) {
                val heightOffset = if (RVUtil.isVertical(recyclerView)) mHeight else 0
                val widthOffset = if (!RVUtil.isVertical(recyclerView)) mWidth else 0
                var top = isHeader
                if (RVUtil.isReverse(recyclerView)) {
                    top = !isHeader
                }
                if (top) {
                    outRect.top = heightOffset
                    outRect.left = widthOffset
                } else {
                    outRect.bottom = heightOffset
                    outRect.right = widthOffset
                }
            }
        }
    }
}