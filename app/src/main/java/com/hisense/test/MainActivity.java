package com.hisense.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hisense.test.server.HttpServer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    boolean flag = false;
    private static int UPDATE_VIDEO_FILE_MAX_SIZE = 450;//unit is M
    private HttpServer server;

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
        button.setText(getLocalIpStr(this));

        server = new HttpServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //获取IP地址
    public static String getLocalIpStr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d(TAG, "wifi info ip address:" + wifiInfo.getIpAddress());
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }
}
