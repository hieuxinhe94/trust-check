package com.example.trustcheck.ui.views.custom

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.ui.helper.DBHelper

class CustomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)
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