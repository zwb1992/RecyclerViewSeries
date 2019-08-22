package com.zwb.recyclerviewseries.section

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.zwb.recyclerviewlibrary.RVUtil
import com.zwb.recyclerviewlibrary.SectionedRecyclerViewAdapter
import com.zwb.recyclerviewseries.R
import kotlinx.android.synthetic.main.section_head.view.*

/**
 * @ author : zhouweibin
 * @ time: 2019/8/22 13:56.
 * @ desc:结合SectionedRecyclerViewAdapter使用，悬停在顶部
 * 注意：高度与分组header保持一致 layoutManager只能是LinearLayoutManager 否则计算会出问题
 **/
class CityStickyHeader : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 已关联recyclerView
     */
    private var isAttached = false

    private var recyclerView: RecyclerView? = null

    private var x = 0
    private var y = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.section_head, this, true)
        x = translationX.toInt()
        y = translationY.toInt()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed && isAttached) {
            onScrollChanged()
        }
    }

    /**
     * 与recyclerView关联在一起
     */
    fun attach(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        isAttached = true

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
        val adapter = recyclerView?.adapter
        if (adapter == null || adapter !is SectionedRecyclerViewAdapter) {
            return
        }
        val hasData = RVUtil.hasData(recyclerView)
        if (!hasData) {
            visibility = View.GONE
            return
        }
        // 第一组的header还没有到顶部 隐藏
        val firstPos = RVUtil.getFirstVisibleItemPosition(recyclerView)
        if (firstPos == 0) {
            val firView = recyclerView?.findViewHolderForAdapterPosition(firstPos)
            if (firView != null && firView.itemView.top > 0) {
                visibility = View.GONE
                return
            }
        }
        visibility = View.VISIBLE
        translationY = y.toFloat()
        translationX = x.toFloat()
        // 显示当前组的head信息
        tvSectionHead.text = adapter.getGroupHeaderTagByPos(firstPos).toString()
        // 第二个view是header 说明此时需要顶上去
        if (adapter.isHeader(firstPos + 1)) {
            val secView = recyclerView?.findViewHolderForAdapterPosition(firstPos + 1)
            if (secView != null) {
                val top = secView.itemView.top
                translationY = (top - height).toFloat()
            }
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
        recyclerView = null
    }
}