package com.zongke.hapilolauncher.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zongke.hapilolauncher.R;

/**
 * Created by dgg1 on 2017/11/2.
 */

public class RushWorkDialog extends Dialog {
    public RushWorkDialog(@NonNull Context context) {
        this(context, 0);
    }

    public RushWorkDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.DialogTheme);
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setGravity(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Params.gravity = Gravity.CENTER_HORIZONTAL;
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.rush_work_dialog_bg);
        imageView.setLayoutParams(Params);
        ll.setLayoutParams(layoutParams);
        ll.addView(imageView);
        this.setContentView(ll);
    }

}
