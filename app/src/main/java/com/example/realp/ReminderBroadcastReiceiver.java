package com.example.realp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderBroadcastReiceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String text=intent.getStringExtra("text");
        int num=intent.getIntExtra("notinum",0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"noti")
                .setSmallIcon(R.drawable.mainicon)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat nm= NotificationManagerCompat.from(context);
        nm.notify(num,builder.build());
    }
}
