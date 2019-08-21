package com.zwb.recyclerviewlibrary

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @ author : zhouweibin
 * @ time: 2019/8/21 15:01.
 * @ desc: 可以为部分item添加header footer （城市列表）
 **/
abstract class SectionedRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
    protected fun isHeader(position: Int): Boolean {
        val headerPos = mutableListOf<Int>()
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
    protected fun isFooter(position: Int): Boolean {
        val headerPos = mutableListOf<Int>()
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
                headerPos.add(totalCount)
                totalCount++
            }
        }
        return headerPos.contains(position)
    }

    /**
     * 获取分组索引
     * @param position 整个列表的索引
     */
    protected fun getGroupIndex(position: Int): Int {
        // 存放分组信息,例如;0-00-0 1-111-1 2-22  333-3  代表：头部+内容+尾部
        val groupPos = mutableListOf<Int>()
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

    /**
     * 获取第几组的开始位置
     * @param position 整个列表的索引 从0开始 传入1 代表获取第0组的数量
     */
    fun getGroupPosition(groupIndex: Int): Int {
        if (groupIndex < 1) {
            return 0
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
        return totalCount
    }

    /**
     * 该组数据是否展开
     * 默认全部展开
     * @param position 整个列表的索引
     */
    protected open fun expandabled(groupIndex: Int) = true
}