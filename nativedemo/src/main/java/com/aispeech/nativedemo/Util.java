package com.aispeech.nativedemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

public class Util {
    public static Notification pupNotification(Context mcontext, PendingIntent pi, String state){
        Notification notification = null;
        NotificationManager mNotificationManager;

        String id ="channel_service";
        CharSequence name = "aispeech recorder";
        int importance =NotificationManager.IMPORTANCE_HIGH;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mNotificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
                notification = new Notification.Builder(mcontext,id)
                        .setContentTitle("DUI Native Demo")
                        .setContentText(state)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        .build();
            }
        }else {
            notification = new Notification.Builder(mcontext)
                    .setContentTitle("DUI Native Demo")
                    .setContentText(state)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .build();
        }
        return notification;
    }
}
