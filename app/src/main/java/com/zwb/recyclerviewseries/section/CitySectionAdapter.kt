package com.zwb.recyclerviewseries.section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zwb.recyclerviewlibrary.SectionedRecyclerViewAdapter
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.section.bean.AreaCode
import com.zwb.recyclerviewseries.section.bean.City

/**
 * @ author : zhouweibin
 * @ time: 2019/8/21 16:27.
 * @ desc:
 **/
class CitySectionAdapter(var cities: MutableList<City>) : SectionedRecyclerViewAdapter() {

    val HEADER_TYPE = 1
    val FOOTER_TYPE = 2
    val SECTION_TYPE = 3

    override fun onBindSectionHeaderViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int) {
        if (holder is SectionHeadVH) {
            holder.setData(cities[groupIndex], groupIndex)
        }
    }

    override fun onBindSectionFooterViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int) {
        if (holder is SectionFootVH) {
            holder.setData(cities[groupIndex])
        }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, groupIndex: Int, sectionIndex: Int) {
        if (holder is SectionItemVH) {
            holder.setData(cities[groupIndex].areaCode[sectionIndex], cities[groupIndex].areaCode.size, sectionIndex)
        }
    }

    override fun isSectionHeaderViewType(viewType: Int) = viewType == HEADER_TYPE

    override fun isSectionFooterViewType(viewType: Int) = viewType == FOOTER_TYPE

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SectionHeadVH(LayoutInflater.from(parent.context).inflate(R.layout.section_head, parent, false),
            object : ExpandListener {
                override fun onExpandabled(expand: Boolean, groupIndex: Int) {
                    val groupPos = getGroupPosition(groupIndex)
                    notifyItemRangeChanged(
                        groupPos + 1,
                        cities[groupIndex].areaCode.size + if (isShowSectionFooter(groupIndex)) 1 else 0
                    )

//                    notifyDataSetChanged()
                }
            })
    }

    override fun onCreateSectionFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SectionFootVH(LayoutInflater.from(parent.context).inflate(R.layout.section_foot, parent, false))
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SectionItemVH(LayoutInflater.from(parent.context).inflate(R.layout.section_item, parent, false))
    }

    override fun getHeaderViewType(groupIndex: Int) = HEADER_TYPE

    override fun getFooterViewType(groupIndex: Int) = FOOTER_TYPE

    override fun getSectionViewType(groupIndex: Int, sectionIndex: Int) = SECTION_TYPE

    override fun getGroupCount() = cities.size

    override fun getSectionCount(groupIndex: Int) = cities[groupIndex].areaCode.size

    override fun isShowSectionHeader(groupIndex: Int) = cities[groupIndex].showHead

    override fun isShowSectionFooter(groupIndex: Int) = cities[groupIndex].showFooter

    class SectionHeadVH(itemView: View, var expandListener: ExpandListener) : RecyclerView.ViewHolder(itemView) {
        var tvExpand: TextView? = null
        var tvSectionHead: TextView? = null
        var city: City? = null
        var groupIndex = 0

        init {
            tvSectionHead = itemView.findViewById(R.id.tvSectionHead)
            tvExpand = itemView.findViewById(R.id.tvExpand)

            tvExpand?.setOnClickListener {
                city?.let { city ->
                    city.expandabled = !city.expandabled
                    tvExpand?.text = if (city.expandabled) "折叠" else "展开"
                    expandListener.onExpandabled(city.expandabled, groupIndex)
                }
            }
        }

        fun setData(city: City, groupIndex: Int) {
            this.city = city
            this.groupIndex = groupIndex
            tvSectionHead?.text = city.groupName
            tvExpand?.text = if (city.expandabled) "折叠" else "展开"
        }

    }

    class SectionFootVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(city: City) {
            itemView.findViewById<TextView>(R.id.tvSectionFoot).text = "查看更多内容"
        }
    }

    class SectionItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(areaCode: AreaCode, itemCount: Int, itemPos: Int) {
            itemView.findViewById<TextView>(R.id.tvContent).text = areaCode.CountryName
            itemView.findViewById<TextView>(R.id.tvIso).text = areaCode.IsoCode
        }
    }

    interface ExpandListener {
        fun onExpandabled(expand: Boolean, groupIndex: Int)
    }

    override fun expandabled(groupIndex: Int): Boolean {
        return cities[groupIndex].expandabled
    }

    override fun getGroupHeaderTag(groupIndex: Int): Any {
        return cities[groupIndex].groupName
    }
}