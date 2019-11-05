package com.zwb.recyclerviewseries.headerfooter.adapter

import com.zwb.recyclerviewlibrary.CommonAdapter
import com.zwb.recyclerviewlibrary.ViewHolder
import com.zwb.recyclerviewseries.R

/**
 * @ author : zhouweibin
 * @ time: 2019/8/16 19:11.
 * @ desc:
 **/
class LinearAdapter : CommonAdapter<String>() {

    override fun getContentView(viewType: Int) = R.layout.header_footer_item

    override fun convert(holder: ViewHolder, t: String, position: Int) {
        holder.setText(R.id.tvContent, t)
    }
}

