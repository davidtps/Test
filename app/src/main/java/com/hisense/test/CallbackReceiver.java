package com.hisense.test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


/**
 * Created by tianpengsheng on 2019年09月18日 15时35分.
 */
public class CallbackReceiver extends BroadcastReceiver {

    private static final String TAG = "CallbackReceiver";
    private static Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent != null && "com.hisense.tv.smartsense.callback".equals(intent.getAction())) {
            int delayTime = intent.getIntExtra("delayTime", 0);//default 0s
            final String action = intent.getStringExtra("action");
            final String packageName = intent.getStringExtra("packageName");
            final String className = intent.getStringExtra("className");
            final String type = intent.getStringExtra("type");//activity  or  receiver or service

            Log.d(TAG, "--------------action :" + action + "  \npackageName:" + packageName + "  \ntype:" + type + "   \nclassName:" + className);


            Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---重启开始  time:" + delayTime);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case "activity":
                            Intent iActivity = new Intent();
                            if (action != null) {
                                Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---action is not null:" + action);
                                iActivity.setAction(action);
                                iActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else {
                                Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---action is  null:" + action);
                                iActivity.addCategory(Intent.CATEGORY_LAUNCHER);
                                iActivity.setAction(Intent.ACTION_MAIN);
                                ComponentName cn = new ComponentName(packageName, className);
                                iActivity.setComponent(cn);
                            }

                            context.startActivity(iActivity);
                            break;
                        case "receiver":
                            Log.d(TAG, "------------------------aaaaaaaaaaaaaaaa---启动--install_restart");
                            Intent iReceiver = new Intent();
                            iReceiver.setAction(action);
                            iReceiver.setPackage(packageName);
                            iReceiver.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                            iReceiver.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            context.sendBroadcast(iReceiver);
                            break;
                        default:
                            break;
                    }
                }
            }, delayTime);

        }
    }
}
