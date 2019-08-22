package com.zwb.recyclerviewlibrary

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @ author : zhouweibin
 * @ time: 2019/8/21 15:01.
 * @ desc: 可以为部分item添加header footer （城市列表）
 * 数据变化后，一定得cleaCache 否则数据会错乱
 **/
abstract class SectionedRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 内部适配器的观察者
     */
    private val mDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            clearCache()
            super.onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            clearCache()
            super.onItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            clearCache()
            super.onItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            clearCache()
            super.onItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            clearCache()
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        }
    }

    init {
        registerAdapterDataObserver(mDataObserver)
    }

    /**
     * 清除缓存数据
     */
    protected fun clearCache() {
        headerPos.clear()
        footerPos.clear()
        groupPos.clear()
        groupCount.clear()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isSectionHeaderViewType(viewType)) {
            onCreateSectionHeaderViewHolder(parent, viewType)
        } else if (isSectionFooterViewType(viewType)) {
            onCreateSectionFooterViewHolder(parent, viewType)
        } else {
            onCreateItemViewHolder(parent, viewType)
        }
    }

    override fun getItemCount() = getTotalCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupIndex = getGroupIndex(position)
        if (isHeader(position)) {
            onBindSectionHeaderViewHolder(holder, groupIndex)
        } else if (isFooter(position)) {
            onBindSectionFooterViewHolder(holder, groupIndex)
        } else {
            onBindItemViewHolder(holder, groupIndex, getSectionIndex(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) {
            getHeaderViewType(getGroupIndex(position))
        } else if (isFooter(position)) {
            getFooterViewType(getGroupIndex(position))
        } else {
            getSectionViewType(getGroupIndex(position), getSectionIndex(position))
        }
    }

    /**
     * 绑定section header 数据
     */
    abstract fun onBindSectionHeaderViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int)

    /**
     * 绑定section footer 数据
     */
    abstract fun onBindSectionFooterViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int)

    /**
     * 绑定section item数据
     */
    abstract fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int, sectionIndex: Int)

    /**
     * 是否是header的布局类型
     */
    abstract fun isSectionHeaderViewType(viewType: Int): Boolean

    /**
     * 是否是footer的布局类型
     */
    abstract fun isSectionFooterViewType(viewType: Int): Boolean

    /**
     * 创建group header ViewHolder
     */
    abstract fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 创建group header ViewHolder
     */
    abstract fun onCreateSectionFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 创建group section ViewHolder
     */
    abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 获取header view的类型
     * @param groupIndex 分组索引
     */
    abstract fun getHeaderViewType(groupIndex: Int): Int

    /**
     * 获取footer view的类型
     * @param groupIndex 分组索引
     */
    abstract fun getFooterViewType(groupIndex: Int): Int

    /**
     * 获取数据item的类型
     * @param groupIndex 分组索引
     */
    abstract fun getSectionViewType(groupIndex: Int, sectionIndex: Int): Int

    /**
     * 获取分组数量
     */
    abstract fun getGroupCount(): Int

    /**
     * 获取分组下item数量
     */
    abstract fun getSectionCount(groupIndex: Int): Int

    /**
     * @param groupIndex 分组索引
     * 该分组是否显示header
     */
    abstract fun isShowSectionHeader(groupIndex: Int): Boolean

    /**
     * @param groupIndex 分组索引
     * 该分组是否显示footer
     */
    abstract fun isShowSectionFooter(groupIndex: Int): Boolean

    /**
     * 获取整个列表的数量--包括各部分的头尾
     */
    protected fun getTotalCount(): Int {
        val groupCount = getGroupCount()
        var totalCount = 0
        for (i in 0 until groupCount) {
            if (isShowSectionHeader(i)) {
                totalCount++
            }
            if (expandabled(i)) {
                totalCount += getSectionCount(i)
            }

            if (isShowSectionFooter(i) && expandabled(i)) {
                totalCount++
            }
        }
        return totalCount
    }

    /**
     * 是否是header
     * @param position 整个列表的索引
     */
    private val headerPos = mutableListOf<Int>()

    fun isHeader(position: Int): Boolean {
        if (headerPos.isNotEmpty()) {
            return headerPos.contains(position)
        }
        headerPos.clear()
        val groupCount = getGroupCount()
        var totalCount = 0
        for (i in 0 until groupCount) {
            if (isShowSectionHeader(i)) {
                headerPos.add(totalCount)
                totalCount++
            }
            if (expandabled(i)) {
                totalCount += getSectionCount(i)
            }

            if (isShowSectionFooter(i) && expandabled(i)) {
                totalCount++
            }
        }
        return headerPos.contains(position)
    }

    /**
     * footer
     * @param position 整个列表的索引
     */
    private val footerPos = mutableListOf<Int>()

    protected fun isFooter(position: Int): Boolean {
        if (footerPos.isNotEmpty()) {
            return footerPos.contains(position)
        }
        footerPos.clear()
        val groupCount = getGroupCount()
        var totalCount = 0
        for (i in 0 until groupCount) {
            if (isShowSectionHeader(i)) {
                totalCount++
            }
            if (expandabled(i)) {
                totalCount += getSectionCount(i)
            }

            if (isShowSectionFooter(i) && expandabled(i)) {
                footerPos.add(totalCount)
                totalCount++
            }
        }
        return footerPos.contains(position)
    }

    /**
     * 获取分组索引
     * @param position 整个列表的索引
     */
    private val groupPos = mutableListOf<Int>()

    protected fun getGroupIndex(position: Int): Int {
        // 存放分组信息,例如;0-00-0 1-111-1 2-22  333-3  代表：头部+内容+尾部
        if (groupPos.isNotEmpty() && groupPos.size > position) {
            return groupPos[position]
        }
        groupPos.clear()
        val groupCount = getGroupCount()
        for (i in 0 until groupCount) {
            if (isShowSectionHeader(i)) {
                groupPos.add(i)
            }
            if (expandabled(i)) {
                for (j in 0 until getSectionCount(i)) {
                    groupPos.add(i)
                }
            }

            if (isShowSectionFooter(i) && expandabled(i)) {
                groupPos.add(i)
            }
        }
        return groupPos[position]
    }

    /**
     * 获取分组下item的索引 例：第一组 第0各位置
     * @param position 整个列表的索引
     */
    protected fun getSectionIndex(position: Int): Int {
        val groupIndex = getGroupIndex(position)
        val groupBeforeCount = getGroupPosition(groupIndex)
        var sectionIndex = position - groupBeforeCount
        if (isShowSectionHeader(groupIndex)) {
            sectionIndex--
        }
        return sectionIndex
    }

    private val groupCount = mutableMapOf<Int, Int>()
    /**
     * 获取第几组的开始位置
     * @param position 整个列表的索引 从0开始 传入1 代表获取第0组的数量
     */
    fun getGroupPosition(groupIndex: Int): Int {
        if (groupIndex < 1) {
            return 0
        }
        val tempCount = groupCount[groupIndex - 1]
        if (tempCount != null) {
            return tempCount
        }
        var totalCount = 0
        for (i in 0 until groupIndex) {
            if (isShowSectionHeader(i)) {
                totalCount++
            }
            if (expandabled(i)) {
                totalCount += getSectionCount(i)
            }

            if (isShowSectionFooter(i) && expandabled(i)) {
                totalCount++
            }
        }
        groupCount[groupIndex - 1] = totalCount
        return totalCount
    }

    /**
     * 该组数据是否展开
     * 默认全部展开
     * @param position 整个列表的索引
     */
    protected open fun expandabled(groupIndex: Int) = true

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        //为了兼容GridLayout
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeader(position)) {
                        return layoutManager.spanCount
                    } else if (isFooter(position)) {
                        return layoutManager.spanCount
                    }
                    return getGridSpanSize(getGroupIndex(position), getSectionIndex(position), position)
                }
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            val position = holder.layoutPosition
            if (isHeader(position) || isFooter(position)) {
                lp.isFullSpan = true
            }
        }
        super.onViewAttachedToWindow(holder)
    }

    /**
     * 获取GridLayoutManager 的SpanSize
     * 注：仅在layoutManager为GridLayoutManager的时候使用
     */
    protected fun getGridSpanSize(groupIndex: Int, sectionIndex: Int, position: Int) = 1

    /**
     * 获取该组标记
     * 用来实现stickyHeader效果
     */
    open fun getGroupHeaderTag(groupIndex: Int): Any = ""

    fun getGroupHeaderTagByPos(position: Int): Any = getGroupHeaderTag(getGroupIndex(position))
}