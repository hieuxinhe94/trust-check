package com.example.trustcheck.ui.views.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.example.trustcheck.ui.views.custom.CustomActivity
import com.example.trustcheck.ui.views.language.MultiLanguageScreen

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_report_new -> {

            }
            R.id.id_custom -> {
                startActivity(Intent(this, CustomActivity::class.java))

            }
            R.id.id_update -> {}
            R.id.id_language -> {
                startActivity(Intent(this, MultiLanguageScreen::class.java))
            }
            R.id.id_donate -> {}

        }
    }

}