package com.hisense.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tianpengsheng on 2019年09月18日 17时43分.
 */
public class EmptyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }
}
