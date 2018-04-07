package com.example.pc.alarmnotificationthroughtimepickerdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Random;

/**
 * Created by pc on 1/4/18.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    int randomId = new Random().nextInt(99) + 20;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_email_black_24dp)
                .setContentTitle("Alarm Activated !!!")
                .setContentText("This is my alarm.")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentInfo("Information");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(randomId, builder.build());
    }
}
