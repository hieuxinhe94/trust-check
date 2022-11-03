package com.example.trustcheck.ui.views.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.example.trustcheck.ui.views.custom.CustomActivity
import com.example.trustcheck.ui.views.language.MultiLanguageScreen
import com.example.trustcheck.ui.views.news.NewsScreen
import com.example.trustcheck.ui.views.report.AddReportScreen
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        initView()
    }

    private fun initView() {
        id_report_new.setOnClickListener(this)
        id_custom.setOnClickListener(this)
        id_update.setOnClickListener(this)
        id_language.setOnClickListener(this)
        id_donate.setOnClickListener(this)
        top_bar_menu.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_report_new -> {
                startActivity(Intent(this, AddReportScreen::class.java))

            }
            R.id.id_custom -> {
                startActivity(Intent(this, CustomActivity::class.java))

            }
            R.id.id_update -> {}
            R.id.id_language -> {
                startActivity(Intent(this, MultiLanguageScreen::class.java))
            }
            R.id.id_donate -> {
                startActivity(Intent(this, NewsScreen::class.java))

            }
            R.id.top_bar_menu -> {
                onBackPressed()
            }

        }
    }

}