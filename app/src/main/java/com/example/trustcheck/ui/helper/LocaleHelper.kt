package com.example.trustcheck.ui.helper

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.example.trustcheck.TrustCheckApplication
import com.example.trustcheck.ui.utils.Constants
import java.util.*

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        persist(language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    private fun persist(language: String) {
        TrustCheckApplication().getPref().edit().putString(Constants.SELECTED_LANGUAGE, language)
            .apply()

    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}