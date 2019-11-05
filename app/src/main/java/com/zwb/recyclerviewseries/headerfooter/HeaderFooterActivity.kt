package com.zwb.recyclerviewseries.headerfooter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.headerfooter.adapter.GridAdapter
import com.zwb.recyclerviewseries.headerfooter.adapter.LinearAdapter
import com.zwb.recyclerviewseries.headerfooter.adapter.StaggeredAdapter
import com.zwb.recyclerviewseries.headerfooter.bean.GridBean
import com.zwb.recyclerviewseries.headerfooter.bean.StaggerdBean
import kotlinx.android.synthetic.main.activity_header_footer.*
import java.util.*

/**
 * 为recyclerview添加头部 尾部
 */
class HeaderFooterActivity : AppCompatActivity() {
    private var layoutManager: LinearLayoutManager? = null
    private var lineaAdapter: LinearAdapter? = null

    private var gridAdapter: GridAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null

    private var staggerdAdapter: StaggeredAdapter? = null
    private var staggerdManager: StaggeredGridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header_footer)
        initLinear()
        header.attach(recyclerView)
        footer.attach(recyclerView, false)
        btReverse.setOnClickListener { reverse() }
        btVertical.setOnClickListener { vertical() }
        btHorizontal.setOnClickListener { horizontal() }
        btGrid.setOnClickListener { grid() }
        btStaggered.setOnClickListener { staggered() }
        header.setOnClickListener {
            Toast.makeText(this, "头部", Toast.LENGTH_SHORT).show()
        }
        footer.setOnClickListener {
            Toast.makeText(this, "尾部", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLinear() {
        lineaAdapter = LinearAdapter()
        recyclerView.adapter = lineaAdapter
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        lineaAdapter?.setDatas(initData())
        lineaAdapter?.setListener {
            onOnItemClickListener { view, any, i ->
                Toast.makeText(this@HeaderFooterActivity,any,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initData(): MutableList<String> {
        val datas = mutableListOf<String>()
        for (i in 0..20) {
            datas.add("测试内容------>$i")
        }
        return datas
    }

    private fun initGrid() {
        gridAdapter = GridAdapter(initGridData())
        gridLayoutManager = GridLayoutManager(this, 6)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = gridAdapter
    }

    private fun initGridData(): MutableList<GridBean> {
        val list = mutableListOf<GridBean>()
        for (i in 0..30) {
            var spanCount = 1
            if (i < 3) {
                spanCount = 3
            } else if (i < 5) {
                spanCount = 2
            } else if (i < 11) {
                spanCount = 6
            } else if (i < 12) {
                spanCount = 1
            } else if (i < 14) {
                spanCount = 2
            } else if (i < 17) {
                spanCount = 3
            } else if (i < 23) {
                spanCount = 6
            } else if (i < 25) {
                spanCount = 2
            } else if (i < 28) {
                spanCount = 1
            } else {
                spanCount = 2
            }
            val bean = GridBean(spanCount, "测试内容---->$i")
            list.add(bean)
        }
        return list
    }


    private fun reverse() {
        val manager = recyclerView.layoutManager
        when (manager) {
            is LinearLayoutManager -> {
                manager.reverseLayout = btReverse.isChecked
            }
            is GridLayoutManager -> {
                manager.reverseLayout = btReverse.isChecked
            }
            is StaggeredGridLayoutManager -> {
                manager.reverseLayout = btReverse.isChecked
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun vertical() {
        val manager = recyclerView.layoutManager
        when (manager) {
            is LinearLayoutManager -> {
                manager.orientation = RecyclerView.VERTICAL
            }
            is GridLayoutManager -> {
                manager.orientation = RecyclerView.VERTICAL
            }
            is StaggeredGridLayoutManager -> {
                manager.orientation = RecyclerView.VERTICAL
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun horizontal() {
        val manager = recyclerView.layoutManager
        when (manager) {
            is LinearLayoutManager -> {
                manager.orientation = RecyclerView.HORIZONTAL
            }
            is GridLayoutManager -> {
                manager.orientation = RecyclerView.HORIZONTAL
            }
            is StaggeredGridLayoutManager -> {
                manager.orientation = RecyclerView.HORIZONTAL
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun grid() {
        initGrid()
    }

    private fun staggered() {
        initStaggered()
    }

    private fun initStaggered() {
        staggerdAdapter = StaggeredAdapter(initStaggeredData())
        staggerdManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        recyclerView.layoutManager = staggerdManager
        recyclerView.adapter = staggerdAdapter
    }

    private fun initStaggeredData(): MutableList<StaggerdBean> {
        val list = mutableListOf<StaggerdBean>()
        for (i in 0..30) {
            val bean = StaggerdBean("测试内容---->$i", Random().nextInt(100) + 120, colors[Random().nextInt(7)])
            list.add(bean)
        }
        return list
    }

    val colors = mutableListOf<String>("#ffffff", "#ff0f0f", "#0000cc", "#112233", "#aa2233", "#0011dd", "#dd3322")
}
