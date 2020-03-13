package com.hisense.test.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by tianpengsheng on 2019年09月11日 10时43分.
 */
public class UpdateApkUtils {

    public static void updateApk(Context context, String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive"
        );
        context.startActivity(intent);
    }

}
