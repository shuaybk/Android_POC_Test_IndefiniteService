package com.shuayb.capstone.android.indefiniteservicetest;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

//Create notification channels at Application level so it's always available from any app component
public class BaseApp extends Application {
    public static String CHANNEL_1_ID = "channel1";
    public static String CHANNEL_1_NAME = "Channel 1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                CHANNEL_1_NAME,
                NotificationManager.IMPORTANCE_LOW
        );
        channel1.setDescription("This is " + CHANNEL_1_NAME);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
