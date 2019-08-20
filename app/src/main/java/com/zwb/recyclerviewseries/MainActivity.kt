package com.zwb.recyclerviewseries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zwb.recyclerviewseries.headerfooter.HeaderFooter2Activity
import com.zwb.recyclerviewseries.headerfooter.HeaderFooterActivity
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
    }
}
