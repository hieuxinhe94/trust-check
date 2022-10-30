package com.example.trustcheck.services

import android.app.IntentService
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.example.trustcheck.ui.utils.Logger

class HeadlessSMSSendService : IntentService(HeadlessSMSSendService::class.java.name) {
    init {
        setIntentRedelivery(true)
    }

    public override fun onHandleIntent(intent: Intent?) {
        if (intent!!.action != TelephonyManager.ACTION_RESPOND_VIA_MESSAGE) {
            return
        }
        val extras = intent.extras ?: return
        val recipientUri = intent.data
        val recipientList = recipientUri!!.schemeSpecificPart
        val recipients = TextUtils.split(recipientList, ",")
        val message = extras.getString(Intent.EXTRA_TEXT, "Message: None")
        val subject = extras.getString(Intent.EXTRA_SUBJECT, "Subject: None")
        val smsManager = SmsManager.getDefault()
        val contentResolver = contentResolver
        val smsSentUri = Uri.parse("content://sms/sent")
        for (recipient in recipients) {
            smsManager.sendTextMessage(recipient, null, message, null, null)
            val values = ContentValues()
            values.put("address", recipient)
            values.put("body", message)
            contentResolver.insert(smsSentUri, values)
        }

        Logger.i(this, "Longkaka", "onHandleIntent")

    }
}