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
    fun setSize(mHeight: Int,mWidth: Int) {
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
        val position = recyclerView.getChildLayoutPosition(view)
        Log.e("zwb", "position === $position")
        val heightOffset = if (RVUtil.isVertical(recyclerView)) mHeight else 0
        val widthOffset = if (!RVUtil.isVertical(recyclerView)) mWidth else 0

        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            if (it is GridLayoutManager) {
                // TODO
                var spanCount = it.spanCount
                Log.e("zwb","grid  spanCount = $spanCount")

            } else {
                var spanCount = 1
                if (it is StaggeredGridLayoutManager) {
                    spanCount = it.spanCount
                }
                // 是否需要分割线
                val needSpace: Boolean
                if (isHeader) {
                    needSpace = position < spanCount
                } else {
                    val count = it.itemCount
                    needSpace = position >= count - spanCount
                    // TODO StaggeredGridLayoutManager 需要检测
                }
                Log.e("zwb", "isHeader = $isHeader needSpace === $needSpace    position = $position  count = ${it.itemCount}  spanCount = $spanCount")
                if (needSpace) {
                    var top = isHeader
                    if(RVUtil.isReverse(recyclerView)){
                        top = !isHeader
                    }
                    if (top) {
                        outRect.top = heightOffset
                        outRect.left = widthOffset
                    } else {
                        outRect.bottom = heightOffset
                        outRect.right = widthOffset
                    }
                }else{}
            }
        }
    }
}