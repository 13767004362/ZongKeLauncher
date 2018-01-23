package com.zongke.hapilolauncher.applist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class AppListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<AppModel>> {
    public static final String TAG = AppListFragment.class.getSimpleName();

    public static AppListFragment newInstance() {
        return new AppListFragment();
    }

    public static final int LOAD_APP_ID = 1;
    private PackageIntentReceiver packageIntentReceiver;
    private AppsLoader appsLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.appsLoader = new AppsLoader(getActivity());
        this.packageIntentReceiver = new PackageIntentReceiver(appsLoader);
        this.getLoaderManager().initLoader(LOAD_APP_ID, null, this);
    }

    @Override
    public Loader<List<AppModel>> onCreateLoader(int id, Bundle args) {
        return appsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<AppModel>> loader, List<AppModel> data) {
        adapter.addData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppModel>> loader) {
        //当重置的时候，传递一个空的集合
        adapter.addData(new ArrayList<AppModel>());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getLoaderManager().destroyLoader(LOAD_APP_ID);
        this.packageIntentReceiver.unRegister(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_applist;
    }

    private AppListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void initAfterActivityCreated(View rootView, Bundle savedInstanceState) {
        recyclerView = rootView.findViewById(R.id.applist_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        adapter = new AppListAdapter();
        this.recyclerView.setAdapter(adapter);
    }
}
