package com.zongke.hapilolauncher.homeScreen;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.base.BaseFragment;



/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class MainMessageFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = MainMessageFragment.class.getSimpleName();

    public static MainMessageFragment newInstance() {
        MainMessageFragment fragment = new MainMessageFragment();

        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected void initAfterActivityCreated(View rootView,  Bundle savedInstanceState) {
        TextView view = rootView.findViewById(R.id.main_message_see_tv);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击查看
            case R.id.main_message_see_tv:
                ComponentName componentName = new ComponentName("com.zhongke.content", 
                        "com.zhongke.content.activity.RewardActivity");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
                break;
            //前往
            case R.id.main_message_look_tv:

                break;
            default:

                break;
        }
    }

}
