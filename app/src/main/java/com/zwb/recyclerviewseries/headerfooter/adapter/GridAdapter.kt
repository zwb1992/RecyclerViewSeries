package com.zwb.recyclerviewseries.headerfooter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.headerfooter.bean.GridBean

/**
 * @ author : zhouweibin
 * @ time: 2019/8/16 19:11.
 * @ desc:
 **/
class GridAdapter(var datas: MutableList<GridBean>) : RecyclerView.Adapter<GridAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_footer_grid_item, parent, false)
        return MyHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        //为了兼容GridLayout
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return layoutManager.spanCount / datas[position].spanCount
                }
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvContent).text = datas[position].text
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

