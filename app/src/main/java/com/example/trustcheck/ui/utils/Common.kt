package com.example.trustcheck.ui.utils

import android.annotation.SuppressLint
import com.example.trustcheck.TrustCheckApplication
import com.example.trustcheck.data.models.BlockingModes

class Common {
    @SuppressLint("CommitPrefEdits")
    fun setFistInstall() {
        TrustCheckApplication().getPref().edit().putBoolean(Constants.FIRST_INSTALL, true).apply()
    }

    fun getFirstInstall(): Boolean {
        return TrustCheckApplication().getPref().getBoolean(Constants.FIRST_INSTALL, false)
    }

    fun blockHiddenNumbers(): Boolean {
        return TrustCheckApplication().getPref().getBoolean(PREF_BLOCK_HIDDEN_NUMBERS, false)
        return false
    }

    fun blockHiddenNumbers(block: Boolean) {
        TrustCheckApplication().getPref().edit()
            .putBoolean(PREF_BLOCK_HIDDEN_NUMBERS, block)
            .apply()
    }

    fun showNotifications(): Boolean {
//        return TrustCheckApplication().getPref().getBoolean(PREF_NOTIFICATIONS, true)
        return false
    }

    fun showNotifications(show: Boolean) {
        TrustCheckApplication().getPref().edit()
            .putBoolean(PREF_NOTIFICATIONS, show)
            .apply()
    }

    var callBlockingMode: Int
        get() = TrustCheckApplication().getPref()
            .getInt(PREF_BLOCKING_MODE, BlockingModes.BLOCK_LIST)
        set(value) {
            TrustCheckApplication().getPref().edit()
                .putInt(PREF_BLOCKING_MODE, value)
                .apply()
        }

    companion object {
        private const val PREF_BLOCK_HIDDEN_NUMBERS = "blockHiddenNumbers"
        private const val PREF_NOTIFICATIONS = "notifications"
        private const val PREF_BLOCKING_MODE = "callBlockingMode"
    }
}