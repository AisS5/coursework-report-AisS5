package com.example.comp2001_aistenphillips;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;


//NotificationsHelper.java structures the notifications for the app. Its purpose is to
//manage the ADMIN and EMPLOYEE notifications
public class NotificationsHelper {

    private DatabaseHelper dbHelper;
    public NotificationsHelper(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    //ADMIN Notifications
    public void sendAdminNotification(Context context, String title){
        String channelId = "ADMIN_requests_channel";                                                                        //The channel ID for the notification channel

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {                                         //Check if the Android version is Oreo (API 26) or higher
            CharSequence name = "Admin Holiday Requests";
            String description = "Notifications for new holiday requests";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);                  //Uses NotificationManager to create the notification channel
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)                          //Create the notification using the NotificationCompat.Builder
                .setContentTitle(title)                                                                             //Set the title of the notification
                .setSmallIcon(R.drawable.notifications_24)                                                          //Set the icon of the notification
                .setAutoCancel(true)                                                                                //Set the notification to automatically cancel when the user taps it
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }

    //EMPLOYEE Notifications
    public void sendEmployeeNotification(Context context, String title){
        String channelId = "EMPLOYEE_requests_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Employee Holiday Requests";
            String description = "Notifications for holiday request status updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.notifications_24)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }






}
