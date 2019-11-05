package com.zwb.recyclerviewlibrary

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @ author : zhouweibin
 * @ time: 2019/11/5 16:17.
 * @ desc:
 **/
class ViewHolder(var view: View, var initListener: Boolean = true) : RecyclerView.ViewHolder(view) {


    private val mViews: SparseArray<View> by lazy { SparseArray<View>() }

    companion object {
        @JvmStatic
        fun get(
            context: Context,
            parent: ViewGroup,
            layoutId: Int,
            initListener: Boolean = true  // 初始化监听器
        ): ViewHolder {
            val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return ViewHolder(view, initListener)
        }
    }

    /**
     * 初始化时，监听item的点击事件和长按事件（避免监听器创建多次）
     */
    init {
        if (initListener) {
            setOnItemClickListener(View.OnClickListener { view ->
                mListener?.mOnItemClickListenerAction?.let {
                    it.invoke(view)
                }
            })

            setOnItemLongClickListener(View.OnLongClickListener { view ->
                mListener?.mOnItemLongClickListenerAction?.let {
                    it.invoke(view)
                }
                return@OnLongClickListener true
            })
        }
    }

    fun <V : View> get(@IdRes viewId: Int): V {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = findViewById(viewId)
        }
        return view as V
    }

    private fun <V : View> findViewById(@IdRes viewId: Int): V {
        return itemView.findViewById(viewId)
    }

    fun setText(@IdRes viewId: Int, text: CharSequence) {
        val textView: TextView = get(viewId)
        textView.text = text
    }

    fun setText(@IdRes viewId: Int, @StringRes textRes: Int) {
        val textView: TextView = get(viewId)
        textView.text = getString(textRes)
    }

    private fun getString(@StringRes ids: Int): CharSequence {
        return itemView.context.resources.getText(ids)
    }


    fun setTextColor(@IdRes viewId: Int, @ColorInt colorInt: Int) {
        val textView: TextView = get(viewId)
        textView.setTextColor(colorInt)
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int) {
        val textView: TextView = get(viewId)
        textView.setTextColor(getColor(colorRes))
    }

    private fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(itemView.context, colorRes)
    }

    fun setTextSize(@IdRes viewId: Int, size: Float) {
        val textView: TextView = get(viewId)
        textView.textSize = size
    }

    fun setVisibility(@IdRes viewId: Int, visibility: Int) {
        val view: View = get(viewId)
        view.visibility = visibility
    }

    fun setSelected(@IdRes viewId: Int, selected: Boolean) {
        val view: View = get(viewId)
        view.isSelected = selected
    }

    fun setEnabled(@IdRes viewId: Int, enable: Boolean) {
        val view: View = get(viewId)
        view.isEnabled = enable
    }

    fun setChecked(@IdRes viewId: Int, checked: Boolean) {
        val view: CompoundButton = get(viewId)
        view.isChecked = checked
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt colorInt: Int) {
        val view: View = get(viewId)
        view.setBackgroundColor(colorInt)
    }

    fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes drawableRes: Int) {
        val view: View = get(viewId)
        view.setBackgroundResource(drawableRes)
    }

    fun setBackground(@IdRes viewId: Int, background: Drawable) {
        val view: View = get(viewId)
        view.background = background
    }

    fun setTag(tag: Any) {
        itemView.tag = tag
    }

    fun setTag(@IdRes viewId: Int, tag: Any) {
        val view: View = get(viewId)
        view.tag = tag
    }

    fun setPositionTag(tag: Int) {
        itemView.setTag(R.id.recycler_default_view_position, tag)
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any) {
        val view: View = get(viewId)
        view.setTag(key, tag)
    }

    fun setOnItemClickListener(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }

    fun setOnItemLongClickListener(listener: View.OnLongClickListener) {
        itemView.setOnLongClickListener(listener)
    }

    fun setOnChildClickListener(@IdRes viewId: Int, listener: View.OnClickListener) {
        get<View>(viewId).setOnClickListener(listener)
    }

    fun setOnChildLongClickListener(@IdRes viewId: Int, listener: View.OnLongClickListener) {
        get<View>(viewId).setOnLongClickListener(listener)
    }

    private var mListener: ListenerBuilder? = null

    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var mOnItemClickListenerAction: ((View) -> Unit)? = null
        internal var mOnItemLongClickListenerAction: ((View) -> Unit)? = null

        fun onOnItemClickListener(action: (View) -> Unit) {
            mOnItemClickListenerAction = action
        }

        fun onOnItemLongClickListener(action: (View) -> Unit) {
            mOnItemLongClickListenerAction = action
        }
    }

}