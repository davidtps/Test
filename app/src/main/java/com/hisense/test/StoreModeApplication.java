package com.hisense.test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Generated application for tinker life cycle
 */
public class StoreModeApplication extends Application {

    private static final String TAG = "StoreModeApplication";
    public static StoreModeApplication sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        //bugly 回收 todo---when release  need delete
//        CrashReport.initCrashReport(this, "597d044629", false);//true--- debug mode

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

        });

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
////        LeakCanary.refWatcher(this).excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
////                .listenerServiceClass(LeakShowService.class).buildAndInstall();
//        LeakCanary.install(this);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}