package com.example.trustcheck.ui.views.language

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.example.trustcheck.ui.helper.LocaleHelper
import kotlinx.android.synthetic.main.activity_multi_language_screen.*

class MultiLanguageScreen : AppCompatActivity(), View.OnClickListener {
    //    private  var en_language : TextView? = null
//    private  var vi_language : TextView? =null
    var color_index: Int = 1

    private fun initView() {
        edt_en.setOnClickListener(this)
        edt_vi.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edt_en -> {
                edt_en.setBackgroundColor(getColor(R.color.main_color))
                edt_vi.setBackgroundColor(getColor(R.color.white))
                color_index = 1
                LocaleHelper.setLocale(this, "en")
            }
            R.id.edt_vi -> {
                edt_vi.setBackgroundColor(getColor(R.color.main_color))
                edt_en.setBackgroundColor(getColor(R.color.white))
                color_index = 2
                LocaleHelper.setLocale(this, "vi")
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_complete -> {

            }
        }
        return super.onOptionsItemSelected(item);
    }
}