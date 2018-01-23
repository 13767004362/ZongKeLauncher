package com.zongke.hapilolauncher.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class ToastUtils {
    public static void  showToast(Context  context, String  content) {
        Toast.makeText(context,content, Toast.LENGTH_SHORT).show();
    }
}
