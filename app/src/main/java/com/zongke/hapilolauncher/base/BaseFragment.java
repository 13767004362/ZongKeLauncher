package com.zongke.hapilolauncher.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public abstract class BaseFragment extends Fragment{
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       this.rootView=inflater.inflate(getLayoutId(),container,false);
        return this.rootView;
    }

    @Override
   public void  onActivityCreated(@Nullable Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);
        initAfterActivityCreated(rootView, savedInstanceState);
    }
    /**
     * 获取制定的布局id
     * @return
     */
    protected abstract int  getLayoutId();
    /**
     * Activity被创建后
     * @param rootView
     * *
     * @param savedInstanceState
     */
    protected abstract void  initAfterActivityCreated(View rootView ,Bundle savedInstanceState );
}
