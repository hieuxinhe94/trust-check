package com.example.trustcheck.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.example.trustcheck.data.models.PhoneNumber;
import com.example.trustcheck.ui.Observer.SmsObservable;
import com.example.trustcheck.ui.helper.DBHelper;
import com.example.trustcheck.ui.utils.Logger;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_DELIVER = Telephony.Sms.Intents.SMS_DELIVER_ACTION;
    public static final String SMS_RECEIVED = Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(SMS_DELIVER)) {
            PendingResult pendingResult = goAsync();
            Task task = new Task(context, pendingResult, intent);
            task.execute();
        } else if (intent.getAction().equals(SMS_RECEIVED)) {

            PendingResult pendingResult = goAsync();
            Task task = new Task(context, pendingResult, intent);
            task.execute();
        }
    }

    private static class Task extends AsyncTask<String, Void, Void> {
        private final Context context;
        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(Context context, PendingResult pendingResult, Intent intent) {
            this.context = context;
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected Void doInBackground(String... v) {
            handleSMS(context, intent);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);

            // Call finish so BroadcastReceiver can be recycled.
            pendingResult.finish();
        }

        private void handleSMS(Context context, Intent intent) {
            Logger.i(this, "Longkaka", "handleSMS");
            Bundle extras = intent.getExtras();

            if (extras == null) {
                return;
            }
            DBHelper dbHelper = new DBHelper(context);


            Object[] pdus = (Object[]) extras.get("pdus");

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String address = sms.getDisplayOriginatingAddress();
                String body = sms.getMessageBody();
                Logger.i(this, "Longkaka", PhoneNumber.INSTANCE.normalizeNumber(address));

                Cursor c = dbHelper.getMessageBlock(PhoneNumber.INSTANCE.normalizeNumber(address));
                boolean blocked = false;
                boolean inList = c.moveToNext();
                Logger.i(this, "Longkaka", String.valueOf(inList));

                if (inList) {
                    blocked = true;
                }

                if (!blocked) {

                    SmsObservable.NewMessageMarker marker = new SmsObservable.NewMessageMarker(address);

                    // Save the message in the SMS inbox table.
                    ContentValues map = new ContentValues();
                    map.put(Telephony.Sms.ADDRESS, address);
                    map.put(Telephony.Sms.BODY, body);

                    context.getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, map);

                    if (SmsObservable.Companion.getInstance().countObservers() == 0) {
                        Logger.i(this, "Longkaka", "Create Noti");
                        // Send user a notification.
                        // TODO
//                        Notification.create(context, address, body);
                    } else {
                        // The app is running. Pass the SMS message to observers.
                        SmsObservable.Companion.getInstance().update(marker);
                    }
                } else {

                    pendingResult.abortBroadcast();
                }
            }
        }
    }
}
