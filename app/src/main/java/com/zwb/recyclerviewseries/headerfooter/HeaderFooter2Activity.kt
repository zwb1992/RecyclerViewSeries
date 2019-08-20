package com.zwb.recyclerviewseries.headerfooter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zwb.recyclerviewlibrary.RVHeaderFooterAdapterWrapper
import com.zwb.recyclerviewseries.R
import com.zwb.recyclerviewseries.headerfooter.adapter.GridAdapter
import com.zwb.recyclerviewseries.headerfooter.adapter.LinearAdapter
import com.zwb.recyclerviewseries.headerfooter.adapter.StaggeredAdapter
import com.zwb.recyclerviewseries.headerfooter.bean.GridBean
import com.zwb.recyclerviewseries.headerfooter.bean.StaggerdBean
import kotlinx.android.synthetic.main.activity_header_footer.recyclerView
import kotlinx.android.synthetic.main.activity_header_footer2.*
import java.util.*

class HeaderFooter2Activity : AppCompatActivity() {

    private var layoutManager: LinearLayoutManager? = null
    private var lineaAdapter: LinearAdapter? = null
    private var rvAdapterWrapper: RVHeaderFooterAdapterWrapper? = null

    private var gridAdapter: GridAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null

    private var staggerdAdapter: StaggeredAdapter? = null
    private var staggerdManager: StaggeredGridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header_footer2)
        initLinear()
        header.attach(recyclerView)
        footer.attach(recyclerView, false)
        addHeader.setOnClickListener {
            addHeader()
        }
        addFooter.setOnClickListener {
            addFooter()
        }

        addData.setOnClickListener {
            addData()
        }
        deleteData.setOnClickListener {
            deleteData()
        }
        grid.setOnClickListener {
            grid()
        }
        staggered.setOnClickListener {
            staggered()
        }
    }

    private fun initLinear() {
        lineaAdapter = LinearAdapter(initData())
        rvAdapterWrapper = RVHeaderFooterAdapterWrapper(lineaAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        addHeader()
        addFooter()
        recyclerView.adapter = rvAdapterWrapper
    }

    val datas = mutableListOf<String>()
    private fun initData(): MutableList<String> {
        for (i in 0..20) {
            datas.add("测试内容------>$i")
        }
        return datas
    }

    private fun addHeader() {
        val view = layoutInflater.inflate(R.layout.header_custom, recyclerView, false)
        rvAdapterWrapper?.let {
            rvAdapterWrapper?.addHeader(view)
            rvAdapterWrapper!!.notifyItemInserted(rvAdapterWrapper!!.getHeadersCount() - 1)
        }
    }

    private fun addFooter() {
        val view = layoutInflater.inflate(R.layout.footer_custom, recyclerView, false)
        rvAdapterWrapper?.let {
            rvAdapterWrapper?.addFooter(view)
            rvAdapterWrapper!!.notifyItemInserted(rvAdapterWrapper!!.itemCount - 1)
        }
    }

    private fun addData() {
        val s = "手动添加的数据"
        datas.add(2, s)
        rvAdapterWrapper?.let {
            rvAdapterWrapper!!.notifyItemInserted(rvAdapterWrapper!!.getHeadersCount() + 2)
        }
    }

    private fun deleteData() {
        datas.removeAt(2)
        datas.removeAt(2)
        datas.removeAt(2)
        rvAdapterWrapper?.let {
            rvAdapterWrapper!!.notifyItemRangeRemoved(rvAdapterWrapper!!.getHeadersCount() + 2, 3)
        }
    }

    private fun grid() {
        initGrid()
    }

    private fun initGrid() {
        gridAdapter = GridAdapter(initGridData())
        rvAdapterWrapper = RVHeaderFooterAdapterWrapper(gridAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        gridLayoutManager = GridLayoutManager(this, 6)
        recyclerView.layoutManager = gridLayoutManager
        addHeader()
        addFooter()
        recyclerView.adapter = rvAdapterWrapper
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

    private fun staggered() {
        initStaggered()
    }

    private fun initStaggered() {
        staggerdAdapter = StaggeredAdapter(initStaggeredData())
        rvAdapterWrapper =
            RVHeaderFooterAdapterWrapper(staggerdAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        staggerdManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        recyclerView.layoutManager = staggerdManager
        addHeader()
        addFooter()
        recyclerView.adapter = rvAdapterWrapper
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
