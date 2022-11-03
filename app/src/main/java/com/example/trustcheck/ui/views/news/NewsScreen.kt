package com.example.trustcheck.ui.views.news

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart

class NewsScreen : AppCompatActivity(), View.OnClickListener {

    private var liveChart: LiveChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_screen)
        liveChart = findViewById(R.id.live_chart)
        init()
    }

    private fun init() {
        val dataset = Dataset(
            mutableListOf(
                DataPoint(0f, 1f),
                DataPoint(1f, 3f),
                DataPoint(2f, 6f)
            )
        )
        liveChart?.setDataset(dataset)?.drawYBounds()
            ?.drawBaseline()
            ?.drawFill()
            ?.drawDataset()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.top_bar_news -> {
                onBackPressed()
            }
        }
    }
}
