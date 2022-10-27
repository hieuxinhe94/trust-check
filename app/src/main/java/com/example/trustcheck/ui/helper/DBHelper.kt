package com.example.trustcheck.ui.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.trustcheck.data.models.PhoneData

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME_PHONE_DATA + " ("
                + ADDRESS_ID + " TEXT NOT NULL PRIMARY KEY, " +
                TYPE_ID + " TEXT," +
                PREDICT_TYPE + " TEXT," +
                PREDICT_VAL + " TEXT," +
                LAST_ACTIVE + " TEXT," +
                LAST_REPORT + " TEXT," +
                FREQ_INDEX + " TEXT," +
                LAST_SYNC + " TEXT"
                + ")")


        val query2 = ("CREATE TABLE " + TABLE_NAME_MESSAGE_DATA + " ("
                + ADDRESS_ID + " TEXT NOT NULL PRIMARY KEY, " +
                TYPE_ID + " TEXT," +
                PREDICT_TYPE + " TEXT," +
                PREDICT_VAL + " TEXT," +
                CONTENT + " TEXT," +
                LAST_ACTIVE + " TEXT," +
                LAST_REPORT + " TEXT," +
                FREQ_INDEX + " TEXT," +
                LAST_SYNC + " TEXT"
                + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHONE_DATA)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addPhoneData(phoneData: PhoneData) {

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        Log.e("Longkaka", phoneData.addressId.toString())

        values.put(ADDRESS_ID, phoneData.addressId)
        values.put(TYPE_ID, phoneData.typeId)
        values.put(PREDICT_TYPE, phoneData.predictType)
        values.put(PREDICT_VAL, phoneData.predictvar)
        values.put(LAST_ACTIVE, phoneData.lastActive)
        values.put(LAST_REPORT, phoneData.lastReport)
        values.put(FREQ_INDEX, phoneData.freqIndex)
        values.put(LAST_SYNC, phoneData.lastSync)


        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME_PHONE_DATA, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    fun addMessageData(phoneData: PhoneData) {

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        Log.e("Longkaka", phoneData.addressId.toString())

        values.put(ADDRESS_ID, phoneData.addressId)
        values.put(TYPE_ID, phoneData.typeId)
        values.put(PREDICT_TYPE, phoneData.predictType)
        values.put(CONTENT, "")

        values.put(PREDICT_VAL, phoneData.predictvar)
        values.put(LAST_ACTIVE, phoneData.lastActive)
        values.put(LAST_REPORT, phoneData.lastReport)
        values.put(FREQ_INDEX, phoneData.freqIndex)
        values.put(LAST_SYNC, phoneData.lastSync)


        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME_MESSAGE_DATA, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getPhoneData(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_PHONE_DATA, null)

    }

    fun getMessageBlock(address: String?): Cursor? {
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.query(
            TABLE_NAME_PHONE_DATA,
            null,
            "? LIKE " + ADDRESS_ID,
            arrayOf(address),
            null,
            null,
            null
        )

    }


    companion object {

        private val DATABASE_NAME = "TrustCheck"

        private val DATABASE_VERSION = 1

        // below is the variable for table name
        @JvmField
        val TABLE_NAME_PHONE_DATA = "PhoneData"

        @JvmField
        val TABLE_NAME_MESSAGE_DATA = "MessageData"

        @JvmField
        val TABLE_NAME_REPORT_DATA = "ReportData"

        @JvmField
        val TABLE_NAME_CATEGORY = "Category"

        @JvmField
        val ADDRESS_ID = "address_id"

        @JvmField
        val TYPE_ID = "type_id"

        @JvmField
        val PREDICT_TYPE = "predict_type"

        @JvmField
        val PREDICT_VAL = "predict_val"

        @JvmField
        val LAST_ACTIVE = "last_active"

        @JvmField
        val LAST_REPORT = "last_report"

        @JvmField
        val FREQ_INDEX = "freq_index"

        @JvmField
        val LAST_SYNC = "last_sync"

        @JvmField
        val TITLE = "title"

        @JvmField
        val CONTENT = "content"

        @JvmField
        val DATE_REPORT = "date_report"

        @JvmField
        val AUTHOR_NAME = "author_name"

        @JvmField
        val AUTHOR_ADDRESS = "author_address"

        @JvmField
        val NAME = "name"

        @JvmField
        val DESCRIPTION = "description"

    }
}