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
import kotlinx.android.synthetic.main.activity_add_report_screen.*

class AddReportScreen : AppCompatActivity(), View.OnClickListener {

    private var recyclerView: RecyclerView? = null
    private var addReportAdapter: AddReportAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report_screen)
        initView()
        displayWarning()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        topAppBar.setOnClickListener(this)
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
        val w1 = RecentWarning("ứng dụng lừa đảo")
        val w2 = RecentWarning("ứng dụng scam")
        val w3 = RecentWarning("ứng dụng vớ vẩn")
        val w4 = RecentWarning("ứng dụng gây hại")
        val w5 = RecentWarning("ứng dụng nghe lén")
        listWarning.add(w1)
        listWarning.add(w2)
        listWarning.add(w3)
        listWarning.add(w4)
        listWarning.add(w5)
        addReportAdapter = AddReportAdapter(listWarning)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView?.adapter = addReportAdapter
    }
}