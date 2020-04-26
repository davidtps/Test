package com.hisense.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


/**
 * Created by tianpengsheng on 2019年09月18日 15时35分.
 */
public class InstallResultReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallResultReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent != null && "com.hisense.storemode.install_storemode_app".equals(intent.getAction())) {
            int time = intent.getIntExtra("time", 20);
            Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---重启开始  time:" + time);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---启动");
                    Intent i = new Intent();
                    i.setAction("com.hisense.storemode.install_restart");
                    i.setPackage("com.hisense.storemode");
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    context.sendBroadcast(i);
                }
            }, time * 1000);
        }
    }
}
