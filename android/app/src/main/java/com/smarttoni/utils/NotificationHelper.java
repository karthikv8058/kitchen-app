package com.smarttoni.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.smarttoni.MainActivity;
import com.smarttoni.R;
import com.smarttoni.utils.WifiUtils;

public class NotificationHelper {

    public static Notification getServerNotification(Context context) {

        String address = WifiUtils.getLocalIpAddress();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "23")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("smartTONi")
                .setContentText("smartTONi Server is Running on " + address)
                .setTicker("TICKER")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("23", "SmartKitchen", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("SmartKitchenServer");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        return notification;
    }

}
