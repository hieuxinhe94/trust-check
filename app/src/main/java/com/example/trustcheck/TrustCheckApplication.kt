package com.example.trustcheck

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.trustcheck.ui.utils.Constants
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TrustCheckApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(
            "TrustCheckApplication",
            "#onCreate"
        )
        ctx = applicationContext

        initPref()
        FirebaseApp.initializeApp(applicationContext)
        syncDatabase();
    }

    private fun initPref() {
        pref = ctx?.getSharedPreferences(
            Constants.SHARE_PREFERENCES,
            MODE_PRIVATE
        )!!
    }

    fun syncDatabase() {

        val db = FirebaseFirestore.getInstance()


        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        db.firestoreSettings = settings
    }

    fun getPref(): SharedPreferences {
        return pref!!
    }


    companion object {
        var ctx: Context? = null
        var pref: SharedPreferences? = null

    }
}