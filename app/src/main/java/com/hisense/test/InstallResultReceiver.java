package com.hisense.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by tianpengsheng on 2019年09月18日 15时35分.
 */
public class InstallResultReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallResultReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS,
                    PackageInstaller.STATUS_FAILURE);
            if (status == PackageInstaller.STATUS_SUCCESS) {
                // success
                Toast.makeText(context, "sucdccccc", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "install success");
            } else {
                Log.e(TAG, "install failed " + intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE));
            }
        }
    }
}
