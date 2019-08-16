package com.zwb.recyclerviewseries.headerfooter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zwb.recyclerviewseries.R

/**
 * @ author : zhouweibin
 * @ time: 2019/8/16 19:11.
 * @ desc:
 **/
class LinearAdapter(var datas: MutableList<String>) : RecyclerView.Adapter<LinearAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_footer_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvContent).text = datas[position]
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

