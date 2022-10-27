package com.example.trustcheck.ui.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.internal.telephony.ITelephony;
import com.example.trustcheck.R;
import com.example.trustcheck.data.models.BlockingModes;
import com.example.trustcheck.data.models.Number;
import com.example.trustcheck.ui.Observer.BlacklistObserver;
import com.example.trustcheck.ui.helper.DBHelper;
import com.example.trustcheck.ui.utils.Common;
import com.example.trustcheck.ui.views.home.HomeActivity;

import java.lang.reflect.Method;


public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "NoPhoneSpam";

    private static final int NOTIFY_REJECTED = 0;
    private static boolean AlreadyOnCall = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction()) &&
                intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Common common = new Common();

            if (common.getCallBlockingMode() != BlockingModes.ALLOW_ALL) {

                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (incomingNumber == null)
                    return;

                Log.i(TAG, "Received call: " + incomingNumber);


                if (TextUtils.isEmpty(incomingNumber)) {
                    if (common.blockHiddenNumbers())
                        rejectCall(context, null, context.getString(R.string.receiver_notify_private_number));

                } else if (common.getCallBlockingMode() == BlockingModes.BLOCK_ALL) {
                    Log.i(TAG, "Block all Calls: " + incomingNumber);
                    Number number;
                    if (isNumberPresentInContacts(context, incomingNumber)) {
                        String name = getCallerID(context, incomingNumber);
                        number = new Number(incomingNumber, name);
                    } else {
                        number = new Number(incomingNumber);
                    }
                    rejectCall(context, number, context.getString(R.string.receiver_notify_no_call_allowed));
                }
                // allow calls only from contacts
                else if (common.getCallBlockingMode() == BlockingModes.ALLOW_CONTACTS) {
                    if (!isNumberPresentInContacts(context, incomingNumber)) {
                        Log.i(TAG, "Number not in contacts: " + incomingNumber);
                        rejectCall(context, new Number(incomingNumber), context.getString(R.string.receiver_notify_not_found_in_contacts));
                    }
                } else {
                    DBHelper dbHelper = new DBHelper(context);
                    try {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor c = db.query(DBHelper.TABLE_NAME_PHONE_DATA, null, "? LIKE " + DBHelper.ADDRESS_ID, new String[]{incomingNumber}, null, null, null);
                        boolean inList = c.moveToNext();
                        // block calls from the numbers stored in list
                        if (inList && common.getCallBlockingMode() == BlockingModes.BLOCK_LIST) {
                            Log.i(TAG, "Number was in list: " + incomingNumber);
                            ContentValues values = new ContentValues();
                            DatabaseUtils.cursorRowToContentValues(c, values);
                            Number number = Number.fromValues(values);

                            rejectCall(context, number, context.getString(R.string.receiver_notify_number_was_in_list));
                            BlacklistObserver.notifyUpdated();

                        }
                        // allow calls only from numbers stored in list
                        else if (!inList && common.getCallBlockingMode() == BlockingModes.ALLOW_ONLY_LIST_CALLS) {
                            Log.i(TAG, "Number was not in list: " + incomingNumber);

                            Number number;
                            if (isNumberPresentInContacts(context, incomingNumber)) {
                                String name = getCallerID(context, incomingNumber);
                                number = new Number(incomingNumber, name);
                            } else {
                                number = new Number(incomingNumber);
                            }

                            rejectCall(context, number, context.getString(R.string.receiver_notify_number_was_not_in_list));
                            BlacklistObserver.notifyUpdated();
                        }
                        c.close();
                    } finally {
                        dbHelper.close();
                    }
                }


            }
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                AlreadyOnCall = true;
            else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE))
                AlreadyOnCall = false;
        }
    }

    @SuppressLint("MissingPermission")
    protected void rejectCall(@NonNull Context context, Number number, String reason) {
        if (!AlreadyOnCall) {
            boolean failed = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

                try {
                    telecomManager.endCall();
                    Log.d(TAG, "Invoked 'endCall' on TelecomManager");
                } catch (Exception e) {
                    Log.e(TAG, "Couldn't end call with TelecomManager", e);
                    failed = true;
                }
            } else {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method m = tm.getClass().getDeclaredMethod("getITelephony");
                    m.setAccessible(true);

                    ITelephony telephony = (ITelephony) m.invoke(tm);

                    telephony.endCall();
                } catch (Exception e) {
                    Log.e(TAG, "Couldn't end call with TelephonyManager", e);
                    failed = true;
                }
            }
            if (failed) {
                Toast.makeText(context, context.getString(R.string.call_blocking_unsupported), Toast.LENGTH_LONG).show();
            }
        }

        Common common = new Common();
        if (common.showNotifications()) {


            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel(
                        "default", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription(reason);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notify = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(reason)
                    .setContentText(number != null ? (number.getName() != null ? number.getName() : number.getNumber()) : context.getString(R.string.receiver_notify_private_number))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                    .addPerson("tel:" + number)
                    .setGroup("rejected")
                    .setChannelId("default")
                    .setGroupSummary(true) /* swy: fix notifications not appearing on kitkat: https://stackoverflow.com/a/37070917/674685 */
                    .build();

            String tag = number != null ? number.getNumber() : "private";
            NotificationManagerCompat.from(context).notify(tag, NOTIFY_REJECTED, notify);


        }

    }

    @SuppressLint("MissingPermission")
    private boolean isNumberPresentInContacts(Context context, String incomingNumber) {
        return getCallerID(context, incomingNumber) != null;
    }

    @SuppressLint("Range")
    private String getCallerID(Context context, String incomingNumber) {
        Cursor cursor = null;
        String name = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
            cursor = resolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                Log.i(TAG, "Received call from contact: " + name);

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return name;
    }

}
