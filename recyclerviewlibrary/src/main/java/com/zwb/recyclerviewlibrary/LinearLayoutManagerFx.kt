package com.zwb.recyclerviewlibrary

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @ author : zhouweibin
 * @ time: 2019/8/21 19:45.
 * @ desc:
 **/
class LinearLayoutManagerFx : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    // java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter
    // positionViewHolder{431a7450 position=1 id=-1, oldPos=-1, pLpos:-1 scrap [attachedScrap] tmpDetached no parent}
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}