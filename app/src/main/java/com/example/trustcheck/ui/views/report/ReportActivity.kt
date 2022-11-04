package com.example.trustcheck.ui.views.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.ui.adapter.WarningAdapter
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart

class ReportActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var warningAdapter: WarningAdapter? = null
    private var liveChart: LiveChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        recyclerView = findViewById(R.id.recycler_view)
        liveChart = findViewById(R.id.live_chart)

        displayWarning()
        init()
    }

    private fun displayWarning() {
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val listWarning: MutableList<RecentWarning> = ArrayList()
        val w1 = RecentWarning("","ứng dụng lừa đảo")
        val w2 = RecentWarning("","ứng dụng scam")
        val w3 = RecentWarning("","ứng dụng vớ vẩn")
        val w4 = RecentWarning("","ứng dụng gây hại")
        val w5 = RecentWarning("","ứng dụng nghe lén")
        listWarning.add(w1)
        listWarning.add(w2)
        listWarning.add(w3)
        listWarning.add(w4)
        listWarning.add(w5)
        warningAdapter = WarningAdapter(listWarning)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView?.adapter = warningAdapter
    }

    private fun init(){
        val dataset = Dataset(mutableListOf(
            DataPoint(0f, 1f),
            DataPoint(1f, 3f),
            DataPoint(2f, 6f)))
        liveChart?.setDataset(dataset)?.drawYBounds()
            ?.drawBaseline()
            ?.drawFill()
            ?.drawDataset()
    }
}