package com.example.trustcheck.ui.views

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R

class CallerInfoActivity : Activity() {
    private val TAG = "NoPhoneSpam"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Create system dialog")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caller_info_bubble)

        Handler(Looper.getMainLooper()).postDelayed({
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Information of caller: ").setCancelable(
                false
            ).setPositiveButton(
                "Yes"
            ) { dialog, id -> dialog.cancel() }.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }, 500)

    }
}