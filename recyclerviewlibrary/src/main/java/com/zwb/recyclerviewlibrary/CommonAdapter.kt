package com.zwb.recyclerviewlibrary

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @ author : zhouweibin
 * @ time: 2019/11/5 16:21.
 * @ desc:
 **/
abstract class CommonAdapter<T : Any> : RecyclerView.Adapter<ViewHolder>() {

    private val mDatas: MutableList<T> by lazy { mutableListOf<T>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder =
            ViewHolder.get(parent.context, parent, getContentView(viewType), initListener())
        holder.setListener {
            onOnItemClickListener {
                mListener?.mOnItemClickListenerAction?.invoke(
                    it,
                    it.tag as T,
                    it.getTag(R.id.recycler_default_view_position) as Int
                )
            }

            onOnItemLongClickListener {
                mListener?.mOnItemLongClickListenerAction?.invoke(
                    it,
                    it.tag as T,
                    it.getTag(R.id.recycler_default_view_position) as Int
                )
            }
        }
        return holder
    }

    override fun getItemCount() = mDatas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTag(mDatas[position])
        holder.setPositionTag(position)
        convert(holder, mDatas[position], position)
    }

    abstract fun getContentView(viewType: Int): Int

    abstract fun convert(holder: ViewHolder, t: T, position: Int)

    protected fun initListener() = true

    private var mListener: ListenerBuilder? = null

    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var mOnItemClickListenerAction: ((View, T, Int) -> Unit)? = null
        internal var mOnItemLongClickListenerAction: ((View, T, Int) -> Unit)? = null

        fun onOnItemClickListener(action: (View, T, Int) -> Unit) {
            mOnItemClickListenerAction = action
        }

        fun onOnItemLongClickListener(action: (View, T, Int) -> Unit) {
            mOnItemLongClickListenerAction = action
        }
    }

    /**
     * 刷新数据，初始化数据
     */
    fun setDatas(list: MutableList<T>?) {
        list?.let {
            mDatas.clear()
            mDatas.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 添加数据
     */
    fun add(i: Int, t: T): Boolean {
        if (mDatas.size > i && i > -1) {
            mDatas.add(i, t)
            notifyItemInserted(i)
            return true
        }
        return false
    }

    /**
     * 添加数据
     *
     */
    fun add(i: Int, t: List<T>?): Boolean {
        if (mDatas.size > i && i > -1 && t != null && t.isNotEmpty()) {
            mDatas.addAll(i, t)
            notifyItemRangeInserted(i, t.size)
            return true
        }
        return false
    }

    /**
     * 更新一个数据
     */
    fun update(t: T?): Int {
        if (mDatas.isNotEmpty() && t != null) {
            for (i in mDatas.indices) {
                val temp = mDatas[i]
                if (t == temp) {
                    mDatas[i] = t
                    notifyItemChanged(i)
                    return i
                }
            }
        }
        return -1
    }

    fun update(t: T?, pos: Int) {
        if (mDatas.isNotEmpty() && t != null) {
            mDatas[pos] = t
            notifyItemChanged(pos)
        }
    }

    fun notifyItem(pos: Int) {
        notifyItemChanged(pos)
    }

    fun update(startIndex: Int, list: List<T>?): Boolean {
        if (mDatas.isNotEmpty() && list != null && list.isNotEmpty() && startIndex >= 0 && startIndex <= mDatas.size) {
            val endIndex =
                if (mDatas.size >= startIndex + list.size) startIndex + list.size else mDatas.size
            mDatas.subList(startIndex, endIndex).clear()
            mDatas.addAll(startIndex, list)
            notifyItemRangeChanged(startIndex, list.size)
            return true
        }
        return false
    }

    /**
     * 删除数据
     */
    fun remove(i: Int): Boolean {
        if (mDatas.size > i && i > -1) {
            mDatas.removeAt(i)
            notifyItemRemoved(i)
            return true
        }
        return false
    }

    /**
     * 加载更多数据
     *
     * @param list
     */
    fun addDatas(list: List<T>?) {
        list?.let {
            if (it.isNotEmpty()) {
                val startIndex = mDatas.size
                mDatas.addAll(it)
                notifyItemRangeInserted(startIndex, it.size)
            }
        }
    }


    fun getDatas(): List<T> {
        return mDatas
    }

    fun getItem(position: Int): T? {
        return if (position > -1 && mDatas.size > position) {
            mDatas[position]
        } else null
    }
}