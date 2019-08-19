package com.zwb.recyclerviewseries.headerfooter.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.headerfooter.bean.StaggerdBean

/**
 * @ author : zhouweibin
 * @ time: 2019/8/16 19:11.
 * @ desc:
 **/
class StaggeredAdapter(var datas: MutableList<StaggerdBean>) : RecyclerView.Adapter<StaggeredAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_footer_grid_item, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvContent).text = datas[position].text
        holder.itemView.findViewById<TextView>(R.id.tvContent).setBackgroundColor(Color.parseColor(datas[position].color))
        holder.itemView.setBackgroundColor(Color.parseColor(datas[position].color))
        val params = holder.itemView.layoutParams
        val den = holder.itemView.context.resources.displayMetrics.density
        params.height = (datas[position].height * den).toInt()
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

