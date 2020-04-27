package com.shuayb.capstone.android.indefiniteservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = (TextView)findViewById(R.id.timerText);
    }

    public void onStartButtonClick(View v) {

    }

    public void onStopButtonClick(View v) {

    }
}
