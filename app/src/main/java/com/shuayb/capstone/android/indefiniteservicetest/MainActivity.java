package com.shuayb.capstone.android.indefiniteservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private TextView timerText;
    private TimerService timerService;
    boolean isBound = false;
    private Thread updateThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        startUpdateThread();

        timerText = (TextView)findViewById(R.id.timerText);
    }

    public void onStartButtonClick(View v) {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }

    public void onStopButtonClick(View v) {
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent); //should pause service instead of stopping
        timerService.stopCounterThread();
    }

    private void startUpdateThread() {
        if (updateThread == null) {
            updateThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!updateThread.isInterrupted()) {
                            Log.d(TAG, "Running update thread");
                            if (isBound) {
                                final int count = timerService.getCounter();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        timerText.setText("" + count);
                                    }
                                });
                            }
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        Log.w(TAG, "Update Thread Error: " + e.toString());
                    }
                }
            };
            updateThread.start();
        }
    }

    @Override
    protected void onDestroy() {
        updateThread.interrupt();
        super.onDestroy();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.TimerServiceBinder binder = (TimerService.TimerServiceBinder) service;
            timerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.w(TAG, "The service has disconnected");
        }
    };
}
