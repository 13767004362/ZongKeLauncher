package com.zongke.hapilolauncher.homeScreen;

import android.os.Bundle;
import android.view.View;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.base.BaseFragment;


/**
 * Created by ${xingen} on 2017/7/5.
 * 显示主页中个人信息和排名
 */

public class MainPersonFragment extends BaseFragment {
    public static final String TAG=MainPersonFragment.class.getSimpleName();
    public static MainPersonFragment  newInstance(){
        return  new MainPersonFragment();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_person;
    }

    @Override
    protected void initAfterActivityCreated(View rootView, Bundle savedInstanceState) {

    }
}
