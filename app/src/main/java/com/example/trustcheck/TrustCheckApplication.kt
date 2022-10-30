package com.example.trustcheck

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.trustcheck.ui.utils.Constants
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
    }

    private fun initPref() {
        pref = ctx?.getSharedPreferences(
            Constants.SHARE_PREFERENCES,
            MODE_PRIVATE
        )!!
    }

    fun getPref(): SharedPreferences {
        return pref!!
    }


    companion object {
        var ctx: Context? = null
        var pref: SharedPreferences? = null

    }
}