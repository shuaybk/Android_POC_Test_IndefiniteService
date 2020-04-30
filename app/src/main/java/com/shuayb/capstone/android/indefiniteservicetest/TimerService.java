package com.shuayb.capstone.android.indefiniteservicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {

    private final String TAG = TimerService.class.getCanonicalName();
    private final IBinder binder = new TimerServiceBinder();
    private final int NOTIFICATION_ID = 1;

    private int counter = 0;
    private Thread counterThread;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    private Notification notification;
    private Context mContext;

    @Override
    public void onCreate() {
        counter = 0;
        mContext = getApplicationContext();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        startCounterThread();
        return START_STICKY;
    }

    private void startCounterThread() {
        if (counterThread == null) {
            counterThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!counterThread.isInterrupted()) {
                            Log.d(TAG, "Counter = " + counter);
                            counter++;
                            updateNotification();
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        Log.w(TAG, "Error while running counter thread:" + e.toString());
                    }
                }
            };
            counterThread.start();
        }
    }

    public void stopCounterThread() {
        if (counterThread != null) {
            counterThread.interrupt();
            counterThread = null;
        }
    }

    private void createNotification() {
        if (notificationBuilder == null) {
            notificationManager = NotificationManagerCompat.from(mContext);
            notificationBuilder = new NotificationCompat.Builder(mContext, BaseApp.CHANNEL_1_ID);

            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification = notificationBuilder
                    .setSmallIcon(R.drawable.ic_timer_black_24dp)
                    .setContentTitle("Counter")
                    .setContentText("" + counter)
                    .setPriority(NotificationCompat.PRIORITY_LOW)   //Redundant cuz it's in the Channel settings, only used on Android versions below Oreo
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentIntent(pIntent)
                    .setOngoing(true)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            updateNotification();
        }
    }

    public void updateNotification() {
        notification = notificationBuilder
                .setContentText("" + counter)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void clearNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public void onDestroy() {
        stopCounterThread();
        System.out.println("Destroying serviceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        super.onDestroy();
    }

    public class TimerServiceBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }
}
