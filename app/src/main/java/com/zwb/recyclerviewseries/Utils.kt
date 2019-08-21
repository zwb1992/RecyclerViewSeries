package com.zwb.recyclerviewseries

import android.content.Context
import com.alibaba.fastjson.JSONObject
import com.zwb.recyclerviewseries.section.bean.AreaCode
import java.io.BufferedInputStream
import java.lang.Exception

/**
 * @ author : zhouweibin
 * @ time: 2019/8/21 16:57.
 * @ desc:
 **/
object Utils {

    fun loadCity(context: Context): MutableList<AreaCode> {
        try {
            val input = context.assets.open("city.txt")
            val bi = BufferedInputStream(input)
            val s = bi.bufferedReader().readLine()
            return JSONObject.parseArray(s, AreaCode::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }
}