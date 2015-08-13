package com.zxw.madaily.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sony on 2015/8/8.
 */
public class ViewUtils {

    public static void showMessage(Context context, String msg) {

        if (context == null)
            throw new IllegalArgumentException("Context cannot be null");

        Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
    }
}
