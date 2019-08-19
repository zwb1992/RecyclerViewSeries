package com.zwb.recyclerviewlibrary

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * @ author : zhouweibin
 * @ time: 2019/8/15 16:24.
 * @ desc: 通过平移的方式，添加recyclerView的头部，尾部
 * （跟随recyclerView一起滑动，给用户一种错觉）
 **/
class RVHeaderFooter : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var x = 0
    private var y = 0
    /**
     * 已关联recyclerView
     */
    private var isAttached = false

    private var isHeader = true

    private var recyclerView: RecyclerView? = null

    private var headerFooterDecoration: HFDecoration? = null

    init {
        x = translationX.toInt()
        y = translationY.toInt()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed && isAttached) {
            var heightOffset = 0
            var widthOffset = 0
            if (layoutParams is MarginLayoutParams) {
                val params = layoutParams as MarginLayoutParams
                heightOffset = params.topMargin + params.bottomMargin
                widthOffset = params.leftMargin + params.rightMargin
            }
            recyclerView?.let {
                headerFooterDecoration?.setSize(heightOffset + height, widthOffset + width)
            }
            recyclerView?.invalidateItemDecorations()
            onScrollChanged()
        }
    }


    /**
     * 与recyclerView关联在一起
     */
    fun attach(recyclerView: RecyclerView, isHeader: Boolean = true) {
        this.isHeader = isHeader
        this.recyclerView = recyclerView
        headerFooterDecoration?.let {
            recyclerView.removeItemDecoration(it)
        }
        isAttached = true
        // 添加顶部或者顶部的分割线（用来占位,让头尾布局正好显示在分割线上）
        headerFooterDecoration = HFDecoration(isHeader)
        recyclerView.addItemDecoration(headerFooterDecoration!!)
        recyclerView.removeOnScrollListener(onScrollListener)
        recyclerView.addOnScrollListener(onScrollListener)
        recyclerView.removeOnChildAttachStateChangeListener(onChildAttachStateChange)
        recyclerView.addOnChildAttachStateChangeListener(onChildAttachStateChange)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrollChanged()
        }
    }

    private val onChildAttachStateChange = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            recyclerView?.post {
                recyclerView?.invalidateItemDecorations()
                onScrollChanged()
            }
        }

        override fun onChildViewAttachedToWindow(view: View) = Unit
    }

    /**
     * 布局滚动处理
     */
    private fun onScrollChanged() {
        val hasData = RVUtil.hasData(recyclerView)
        val show =
            isHeader && RVUtil.isFirstRowVisible(recyclerView) || !isHeader && RVUtil.isLastRowVisible(recyclerView)
        visibility = if (hasData && show) View.VISIBLE else View.INVISIBLE
        if (hasData && show) {
            setTransitionXY()
        }
    }

    /**
     * 垂直/水平方向平移
     */
    private fun setTransitionXY() {
        val xy = calculateTranslation()
        // 先回到初始位置，避免方向切换时，布局找不到
        translationY = y.toFloat()
        translationX = x.toFloat()
        if (RVUtil.isVertical(recyclerView)) {
            translationY = xy.toFloat()
        } else {
            translationX = xy.toFloat()
        }
    }


    private fun calculateTranslation(): Int {
        val offset = RVUtil.getScrollOffset(recyclerView, RVUtil.isVertical(recyclerView))
        val scrollRange = RVUtil.getScrollRange(recyclerView, RVUtil.isVertical(recyclerView))
        val size = if (RVUtil.isVertical(recyclerView)) height else width
        // 如果没有反转
        var top = isHeader
        if (RVUtil.isReverse(recyclerView)) {
            top = !isHeader
        }
        return if (top) {
            -offset
        } else {
            scrollRange - offset - size
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destroy()
    }

    private fun destroy() {
        isAttached = false
        recyclerView?.removeOnChildAttachStateChangeListener(onChildAttachStateChange)
        recyclerView?.removeOnScrollListener(onScrollListener)
        headerFooterDecoration?.let {
            recyclerView?.removeItemDecoration(it)
        }
        recyclerView = null
    }
}