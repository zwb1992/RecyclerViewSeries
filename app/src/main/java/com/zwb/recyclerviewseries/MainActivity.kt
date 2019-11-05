package com.zwb.recyclerviewseries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zwb.recyclerviewseries.headerfooter.HeaderFooter2Activity
import com.zwb.recyclerviewseries.headerfooter.HeaderFooterActivity
import com.zwb.recyclerviewseries.layoutmanager.SimpleLayoutManagerActivity
import com.zwb.recyclerviewseries.section.SectionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btHeaderFooter.setOnClickListener {
            startActivity(Intent(this, HeaderFooterActivity::class.java))
        }
        btHeaderFooter2.setOnClickListener {
            startActivity(Intent(this, HeaderFooter2Activity::class.java))
        }
        btSection.setOnClickListener {
            startActivity(Intent(this, SectionActivity::class.java))
        }
        btSimpleLayoutManager.setOnClickListener {
            startActivity(Intent(this, SimpleLayoutManagerActivity::class.java))
            Test().setSpeed(4)
            val result = ("123" == "123").yes {
                Log.e("zwb", "yes----------->")
                "222"
            }.otherwise {
                Log.e("zwb", "otherwise----------->")
                "333"
            }
            Log.e("zwb", "result----------->$result")
        }
    }
}
