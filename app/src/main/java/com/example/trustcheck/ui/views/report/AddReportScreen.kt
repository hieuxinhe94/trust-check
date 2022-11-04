package com.example.trustcheck.ui.views.report

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.ui.adapter.AddReportAdapter
import com.example.trustcheck.ui.adapter.WarningAdapter
import kotlinx.android.synthetic.main.activity_add_report_screen.*
import kotlinx.android.synthetic.main.activity_news_screen.*

class AddReportScreen : AppCompatActivity(), View.OnClickListener {

    private var recyclerView: RecyclerView? = null
    private var warningAdapter: AddReportAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report_screen)
        initView()
        displayWarning()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        //top_bar_news.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.topAppBar -> {
                onBackPressed()

            }
        }
    }

    private fun displayWarning() {
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val listWarning: MutableList<RecentWarning> = ArrayList()
        val w1 = RecentWarning("","Lừa đảo")
        val w2 = RecentWarning("","Làm phiền, quấy rối")
        val w3 = RecentWarning("","Quảng cáo")
        val w4 = RecentWarning("","Tự động - bot")
        listWarning.add(w1)
        listWarning.add(w2)
        listWarning.add(w3)
        listWarning.add(w4)
        warningAdapter = AddReportAdapter(listWarning)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView?.adapter = warningAdapter
    }
}