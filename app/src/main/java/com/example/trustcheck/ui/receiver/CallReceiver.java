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
import com.example.trustcheck.data.models.PhoneData;
import com.example.trustcheck.data.models.Report;
import com.example.trustcheck.services.ContactInfoService;
import com.example.trustcheck.ui.Observer.BlacklistObserver;
import com.example.trustcheck.ui.helper.DBHelper;
import com.example.trustcheck.ui.utils.Common;
import com.example.trustcheck.ui.views.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Method;
import java.util.Calendar;


public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "NoPhoneSpam";
    private static final int NOTIFY_REJECTED = 0;
    private static boolean AlreadyOnCall = false;
    private boolean inBlackList = false;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        String currentState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.i(TAG, "TelephonyManager currentState: " + currentState + " at time: " + Calendar.getInstance().getTime());

        if (currentState.equals(TelephonyManager.CALL_STATE_RINGING)) {
            Log.i(TAG, "CALL_STATE_RINGING 1: " + Calendar.getInstance().getTime());
        }

        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction()) &&
                currentState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Log.i(TAG, "EXTRA_STATE_RINGING 2: " + Calendar.getInstance().getTime());

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

                    try {
                        Log.i(TAG, "CALL_STATE_RINGING Firebase start search: " + Calendar.getInstance().getTime());
                        db.collection("phoneData_VN").document(incomingNumber)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Log.i(TAG, "CALL_STATE_RINGING Firebase onComplete search: " + Calendar.getInstance().getTime());
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                inBlackList = true;

                                                // Display info
                                                displayCallerWarningInfo(currentState, context, incomingNumber, "SPAM");
                                            } else {
                                                Log.d(TAG, "Not found ");
                                                inBlackList = false;
                                            }

                                            // TODO: un-comment rejectCall
                                            // block calls from the numbers stored in list
                                            if (inBlackList && common.getCallBlockingMode() == BlockingModes.BLOCK_LIST) {
                                                Log.i(TAG, "Number was in list: " + incomingNumber);
                                                ContentValues values = new ContentValues();

                                                Number number = Number.fromValues(values);

                                                //rejectCall(context, number, context.getString(R.string.receiver_notify_number_was_in_list));
                                                BlacklistObserver.notifyUpdated();
                                            }
                                            // allow calls only from numbers stored in list
                                            else if (!inBlackList && common.getCallBlockingMode() == BlockingModes.ALLOW_ONLY_LIST_CALLS) {
                                                Log.i(TAG, "Number was not in list: " + incomingNumber);

                                                Number number;
                                                if (isNumberPresentInContacts(context, incomingNumber)) {
                                                    String name = getCallerID(context, incomingNumber);
                                                    number = new Number(incomingNumber, name);
                                                } else {
                                                    number = new Number(incomingNumber);
                                                }

                                                //rejectCall(context, number, context.getString(R.string.receiver_notify_number_was_not_in_list));
                                                BlacklistObserver.notifyUpdated();
                                            }


                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });

                    } catch (Exception ex) {

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

    private void displayCallerWarningInfo(String state, Context ctx,String callerNumber, String callerWarningLabel) {
        Log.i(TAG, "TelephonyManager State: " + state);
        Intent i;

        //if(state.equals(String.valueOf(TelephonyManager.CALL_STATE_RINGING))) {
            i = new Intent(ctx, ContactInfoService.class);
            i.putExtra("callerLabel", callerWarningLabel);
            i.putExtra("callerNumber", callerNumber);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ctx.startForegroundService(i);
            } else {
                ctx.startService(i);
            }
//        } else {
//           i = new Intent(ctx, ContactInfoService.class);
//           ctx.stopService(i);
//        }
    }


}
