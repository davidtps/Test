package com.hisense.test;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    boolean flag = false;
    private static int UPDATE_VIDEO_FILE_MAX_SIZE = 450;//unit is M

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, EmptyService.class);
        startService(intent);

        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hisense.storemode.start");
                intent.setPackage("com.hisense.storemode");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                sendBroadcast(intent);
            }
        });
        findViewById(R.id.start_4k).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hisense.storemode.start_4k");
                intent.setPackage("com.hisense.storemode");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hisense.storemode.finish");
                intent.setPackage("com.hisense.storemode");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.service_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.hisense.storemode", "com.hisense.storemode.StoreModeService"));
                intent.setAction("com.hisense.storemode.start");
                startService(intent);
            }
        });
        findViewById(R.id.service_4k).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.hisense.storemode", "com.hisense.storemode.StoreModeService"));
                intent.setAction("com.hisense.storemode.start_4k");
                startService(intent);
            }
        });

        findViewById(R.id.service_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.hisense.storemode", "com.hisense.storemode.StoreModeService"));
                intent.setAction("com.hisense.storemode.finish");
                startService(intent);
            }
        });

        Button button = findViewById(R.id.service_stop);
        button.setText(getString(R.string.epos_updating_tips));

    }
}
