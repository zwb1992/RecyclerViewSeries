package com.zwb.recyclerviewseries.section

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.zwb.recyclerviewlibrary.LinearLayoutManagerFx
import com.zwb.recyclerviewlibrary.RVHeaderFooterAdapterWrapper
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.Utils
import com.zwb.recyclerviewseries.section.bean.AreaCode
import com.zwb.recyclerviewseries.section.bean.City
import kotlinx.android.synthetic.main.activity_section.*
import kotlinx.android.synthetic.main.section_query.*

class SectionActivity : AppCompatActivity() {
    private var adapter: CitySectionAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)
        header.attach(recyclerView)
        btQuery.setOnClickListener {
            query(edit_query.text.toString())
        }
        loadDta()
    }

    fun loadDta() {
        Thread {
            val list = Utils.loadCity(this)
            runOnUiThread {
                setData(list)
            }
        }.start()
    }

    val cities = mutableListOf<City>()
    fun setData(data: MutableList<AreaCode>) {
        data.forEach { areaCode ->
            var city = cities.find { it.groupName == areaCode.groupName }
            if (city == null) {
                city = City()
                cities.add(city)
            }
            city.groupName = areaCode.groupName
            city.showHead = true
            city.showFooter = true
            city.areaCode.add(areaCode)
        }
        adapter = CitySectionAdapter(cities)
        recyclerView.layoutManager = LinearLayoutManagerFx(this)
//        val rvHeaderFooterAdapterWrapper = RVHeaderFooterAdapterWrapper(adapter)
//        rvHeaderFooterAdapterWrapper.addHeader(layoutInflater.inflate(R.layout.section_query,recyclerView,false))
//        recyclerView.adapter = rvHeaderFooterAdapterWrapper
        recyclerView.adapter = adapter
    }

    fun query(s: String?) {
        if (!s.isNullOrEmpty()) {
            val find = cities.any { it.groupName == s }
            if (find) {
                var groupIndex = -1
                for (i in 0 until cities.size) {
                    if (cities[i].groupName == s) {
                        groupIndex = i
                        break
                    }
                }
                if (groupIndex != -1) {
                    Log.e("zwb", "position = ${adapter!!.getGroupPosition(groupIndex)}")
                    recyclerView.scrollToPosition(adapter!!.itemCount - 1)
                    recyclerView.postDelayed({
                        recyclerView.scrollToPosition(adapter!!.getGroupPosition(groupIndex))
                    }, 50)
                }
            }
        }
    }
}
