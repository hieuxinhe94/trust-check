package com.example.trustcheck.ui.views.custom

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trustcheck.R
import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.ui.helper.DBHelper
import kotlinx.android.synthetic.main.activity_custom.*

class CustomActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)
        txt_yes_report.setOnClickListener(this)
        txt_report_no.setOnClickListener(this)
        txt_off_alarm.setOnClickListener(this)
        txt_reject.setOnClickListener(this)

        txt_off_alarm_sms.setOnClickListener(this)
        txt_auto_delete.setOnClickListener(this)
        txt_app_scan_yes.setOnClickListener(this)
        txt_app_scan_no.setOnClickListener(this)
        toolbar_custom.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_custom -> {
                onBackPressed()

            }
            R.id.txt_yes_report -> {
                txt_yes_report.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_report_no.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )

            }
            R.id.txt_report_no -> {
                txt_report_no.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_yes_report.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )
            }
            R.id.txt_off_alarm -> {
                txt_off_alarm.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_reject.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )


            }
            R.id.txt_reject -> {
                txt_reject.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_off_alarm.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )


            }



            R.id.txt_off_alarm_sms -> {
                txt_off_alarm_sms.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_auto_delete.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )

            }

            R.id.txt_auto_delete -> {
                txt_auto_delete.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_off_alarm_sms.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )

            }

            R.id.txt_app_scan_yes -> {
                txt_app_scan_yes.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_app_scan_no.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )
            }

            R.id.txt_app_scan_no -> {
                txt_app_scan_no.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_main_color
                    )
                )
                txt_app_scan_yes.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.border_white
                    )
                )

            }


        }
    }

    private fun initDb() {
        val db = DBHelper(this)

        var phoneData: PhoneData? = null
        if (phoneData != null) {
            db.addPhoneData(phoneData)
        }

        // Toast to message on the screen
        Toast.makeText(this, " added to database", Toast.LENGTH_LONG).show()


    }

    private fun getDB() {
// creating a DBHelper class
        // and passing context to it
        val db = DBHelper(this)

        // below is the variable for cursor
        // we have called method to get
        // all names from our database
        // and add to name text view
        val cursor = db.getPhoneData()

        // moving the cursor to first position and
        // appending value in the text view
//        cursor!!.moveToFirst()
//        Name.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
//        Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
//
//        // moving our cursor to next
//        // position and appending values
//        while(cursor.moveToNext()){
//            Name.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
//            Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
//        }
//
//        // at last we close our cursor
//        cursor.close()
    }


}