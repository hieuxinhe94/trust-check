package com.example.trustcheck.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.trustcheck.R;
import com.example.trustcheck.ui.views.report.ReportActivity;

public class ContactInfoService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }

    private View view;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();

        String callerLabel = intent.getStringExtra("callerLabel");
        String callerNumber = intent.getStringExtra("callerNumber");
        String contentParsed = intent.getStringExtra("contentParsed");

        String title = callerNumber + " was reported is " + callerLabel + "!";
        String desc = "32 peoples said this number disturb them";

        displayCallerWarningNotification(title, desc, "");

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            Log.i("ContactInfoService", " TYPE_APPLICATION_OVERLAY");
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            Log.i("ContactInfoService", " TYPE_PHONE");
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_caller_info_bubble, null);
        TextView textview = (TextView) view.findViewById(R.id.txt_warning_desc);
        textview.setText(title);

//        ImageView closeIcon = (ImageView)view.findViewById(R.id.close);
//        closeIcon.setImageResource(R.drawable.ic_close);

        ImageView logo = (ImageView) view.findViewById(R.id.imageLogo);
        logo.setImageResource(R.drawable.logo);

        TextView btnSkip = (TextView) view.findViewById(R.id.btnSkip);
        TextView btnBlock = (TextView) view.findViewById(R.id.btnBlock);

        btnSkip.setOnClickListener(view1 -> onDestroy());
        btnBlock.setOnClickListener(view1 -> onDestroy());

        wm.addView(view, params);
        new Handler().postDelayed(() -> {
            onDestroy();
        }, 15000);

        return START_STICKY;
    }

    public void onDestroy() {
        Log.i("ContactInfoService", " onDestroy");
        super.stopSelf();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.removeView(view);
    }

    private void displayCallerWarningNotification(String title, String content, String actionKey) {
        NotificationChannel channel = new NotificationChannel("channel01", "name",
                NotificationManager.IMPORTANCE_HIGH);   // for heads-up notifications
        channel.setDescription("description");

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ReportActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Register channel with system
        //NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);

        // Show the heads-up notification:
        Notification notification = new NotificationCompat.Builder(this, "channel01")
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.COLOR_DEFAULT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                .build();

        // NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }
}
