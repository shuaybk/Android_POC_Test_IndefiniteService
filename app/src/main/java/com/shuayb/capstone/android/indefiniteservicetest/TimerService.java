package com.shuayb.capstone.android.indefiniteservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService extends Service {

    private final String TAG = TimerService.class.getCanonicalName();
    private final IBinder binder = new TimerServiceBinder();

    private int counter = 0;
    private Thread counterThread;

    @Override
    public void onCreate() {
        counter = 0;
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCounterThread();
        createNotification();
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
