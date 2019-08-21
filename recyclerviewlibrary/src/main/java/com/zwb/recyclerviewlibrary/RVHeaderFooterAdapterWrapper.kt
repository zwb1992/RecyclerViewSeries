package com.zwb.recyclerviewlibrary

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @ author : zhouweibin
 * @ time: 2019/8/20 14:36.
 * @ desc: 通过添加viewType的方式添加多个header 多个footer
 **/
class RVHeaderFooterAdapterWrapper(private var mInnerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HeaderFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val mHeaders = SparseArray<View>()   // 头部布局
    private val mFooters = SparseArray<View>()   // 尾部布局

    @JvmField
    val HEADER_TYPE = 1000
    @JvmField
    val FOOTER_TYPE = 2000

    /**
     * 内部适配器的观察者
     */
    private val mDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            super.onChanged()
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            val headerViewsCountCount = getHeadersCount()
            notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount)
        }
    }

    /**
     * 更新适配器
     */
    private fun updateAdapter() {
        mInnerAdapter?.let {
            notifyItemRangeRemoved(getHeadersCount(), mInnerAdapter!!.itemCount)
            mInnerAdapter?.registerAdapterDataObserver(mDataObserver)
            notifyItemRangeInserted(getHeadersCount(), mInnerAdapter!!.itemCount)
        }
    }

    init {
        updateAdapter()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mHeaders.indexOfKey(viewType) >= 0) {
            return HeaderFooterHolder(mHeaders[viewType])
        } else if (mFooters.indexOfKey(viewType) >= 0) {
            return HeaderFooterHolder(mFooters[viewType])
        } else {
            return mInnerAdapter!!.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount() = getHeadersCount() + getRealItemCount() + getFootersCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!isHeaderViewPos(position) && !isFooterViewPos(position)) {
            mInnerAdapter?.onBindViewHolder(holder, getRealPosition(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaders.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFooters.keyAt(position - getHeadersCount() - getRealItemCount())
        } else {
            return getRealItemViewType(position)
        }
    }

    private fun getRealItemViewType(position: Int) = mInnerAdapter?.getItemViewType(position - getHeadersCount()) ?: 0

    /**
     * 是否是头部布局
     */
    private fun isHeaderViewPos(pos: Int) = pos < getHeadersCount()

    /**
     * 是否是尾部布局
     */
    private fun isFooterViewPos(pos: Int) = pos >= getHeadersCount() + getRealItemCount()

    /**
     * 头部数量
     */
    fun getHeadersCount() = mHeaders.size()

    /**
     * 尾部数量
     */
    fun getFootersCount() = mFooters.size()

    /**
     * 真实数据源数量
     */
    private fun getRealItemCount() = mInnerAdapter?.itemCount ?: 0


    fun getRealPosition(position: Int) = position - getHeadersCount()

    /**
     * 设置数据源
     */
    fun setAdapter(innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        this.mInnerAdapter = innerAdapter
        updateAdapter()
    }

    /**
     * 获取数据源适配器
     */
    fun getDataAdapter() = mInnerAdapter


    /**
     * 直接添加header view
     */
    fun addHeader(header: View) {
        mHeaders.append(HEADER_TYPE + getHeadersCount(), header)
    }

    /**
     * 设置第几个位置的header
     */
    fun setHeader(position: Int, header: View) {
        if (position < 0 || position > getHeadersCount()) {
            addHeader(header)
        } else {
            mHeaders.append(HEADER_TYPE + position, header)
        }
    }

    /**
     * footer view
     */
    fun addFooter(header: View) {
        mFooters.append(FOOTER_TYPE + getFootersCount(), header)
    }

    /**
     * 设置第几个位置的footer
     */
    fun setFooter(position: Int, header: View) {
        if (position < 0 || position > getFootersCount()) {
            addFooter(header)
        } else {
            mFooters.append(FOOTER_TYPE + position, header)
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mInnerAdapter?.onAttachedToRecyclerView(recyclerView)
        //为了兼容GridLayout
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderViewPos(position)) {
                        return layoutManager.spanCount
                    } else if (isFooterViewPos(position)) {
                        return layoutManager.spanCount
                    }
                    return spanSizeLookup?.getSpanSize(getRealPosition(position)) ?: 1
                }
            }
        }

    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        mInnerAdapter?.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }
}