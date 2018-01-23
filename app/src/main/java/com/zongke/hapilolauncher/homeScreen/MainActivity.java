package com.zongke.hapilolauncher.homeScreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.sqlbrite.SqlBrite;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhongke.account.AccountManagerClient;
import com.zhongke.account.ClientListener;
import com.zhongke.account.control.BaseManager;
import com.zhongke.account.model.AccountInfo;
import com.zhongke.account.model.ZkLocalMessage;
import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.applist.AppFilterUtil;
import com.zongke.hapilolauncher.base.BaseApplication;
import com.zongke.hapilolauncher.broadcast.StartWindowDialogBroadcastReceiver;
import com.zongke.hapilolauncher.connection.RemoteServicesClient;
import com.zongke.hapilolauncher.db.ContentProviderConfig;
import com.zongke.hapilolauncher.db.ConversionObjectUtils;
import com.zongke.hapilolauncher.db.entity.ActivityDegreeListBean;
import com.zongke.hapilolauncher.library.glide.GlideLoader;
import com.zongke.hapilolauncher.library.retrofit.BuilderMap;
import com.zongke.hapilolauncher.library.retrofit.CommonException;
import com.zongke.hapilolauncher.library.retrofit.ResponseResultListener;
import com.zongke.hapilolauncher.library.retrofit.RetrofitProvider;
import com.zongke.hapilolauncher.library.rxjava.SubscribeUtils;
import com.zongke.hapilolauncher.library.sqlbrite.SQLBriteProvider;
import com.zongke.hapilolauncher.recyclerview.BaseRecyclerView;
import com.zongke.hapilolauncher.scancode.ScanCodeActivity;
import com.zongke.hapilolauncher.service.MusicService;
import com.zongke.hapilolauncher.utils.BitmapUtils;
import com.zongke.hapilolauncher.utils.DisplayUtils;
import com.zongke.hapilolauncher.utils.LauncherUtils;
import com.zongke.hapilolauncher.utils.PermissionUtils;
import com.zongke.hapilolauncher.utils.SystemUIManager;
import com.zongke.hapilolauncher.utils.ToastUtils;
import com.zongke.hapilolauncher.view.CountClock;
import com.zongke.hapilolauncher.view.HorizontalScrollBGView;
import com.zongke.hapilolauncher.view.LauncherRotatingMenu;
import com.zongke.hapilolauncher.view.OpenBoxDialog;
import com.zongke.hapilolauncher.view.PercentProgressBar;
import com.zongke.hapilolauncher.view.RotatingRingMenu;
import com.zongke.hapilolauncher.view.RushWorkDialog;
import com.zongke.hapilolauncher.view.StrokeTextView;
import com.zongke.hapilolauncher.view.WingsWavingLayout;
import com.zongke.hapilolauncher.window.SuspensionWindowManagerUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, LauncherRotatingMenu.LauncherRotatingMenuCallBack, LoaderManager.LoaderCallbacks<Cursor>, ClientListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private HorizontalScrollBGView horizontalScrollBGView;
    private WingsWavingLayout middleWingsWavingLayout, leftWingsWavingLayout, rightWingsWavingLayout;
    private CompositeSubscription compositeSubscription;
    private RotatingRingMenu rotatingRingMenu;
    private ImageView bg_imageView;
    private LauncherRotatingMenu launcherRotatingMenu;
    private StrokeTextView strokeTextView;
    private BaseRecyclerView baseRecyclerView;
    private PercentProgressBar mSeekBarView;
    private RushWorkDialog dialog;
    private LinearLayout le;
    private RetrofitProvider retrofitProvider;
    private MainPersonFragment mainPersonFragment;
    private MainMessageFragment mainMessageFragment;
    private PersonRankingAdapter adapter;
    /**
     * 宝箱部分
     */
    private FrameLayout treasureLay;
    /**
     * 今日成就 按钮
     */
    private ImageView todayDoneBtn;
    /**
     * 宝箱倒计时控件
     */
    private CountClock countClock;
    /**
     * 音乐服务对象
     */
    private MusicService musicService = null;
    /**
     * 加载器的id
     */
    private int loaderId = 110;

    private Subscription subscription;

    /**
     * 绑定服务  服务连接对象
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 绑定了服务
            Log.i("llj", "绑定了背景音乐服务！！！");
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 解绑了音乐服务
            Log.i("llj", "解绑了背景音乐服务！！！");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        if (savedInstanceState != null) {
            recoverState(savedInstanceState);
        } else {
            handlerState();
        }
    }

    /**
     * 测试处理
     */
    private void testActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Intent intent = new Intent();
                                                                    intent.setClassName("com.zhongke.content", "com.zhongke.content.activity.AnswerActivity");
                                                                    intent.putExtra("activityId", "119");
                                                                    startActivity(intent);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        }
                , 2 * 1000);
    }

    private AccountInfo userInfo;
    private BaseManager baseManager = null;

    /**
     * 检查是否设备激活
     */
    private void handlerState() {
        baseManager = com.zhongke.account.control.AccountManager.getInstance(this);
        baseManager.queryAccount(new Subscriber<AccountInfo>() {
            @Override
            public void onCompleted() {
                if (userInfo == null || userInfo.getToken() == null) {
                    //没有扫码激活
                    startScanCode();
                } else {
                    normalState();
                }
            }

            @Override
            public void onError(Throwable e) {
                //没有扫码激活
                startScanCode();
            }

            @Override
            public void onNext(AccountInfo accountInfo) {
                userInfo = accountInfo;
            }
        });
    }

    /**
     * 执行网络请求
     */
    private void executeNetWork() {
        this.retrofitProvider = RetrofitProvider.getInstance();
        Subscription subscription = this.retrofitProvider.getActivityDreeList(BuilderMap.builderActivityList(userInfo.getToken(), 1, 3),
                new ResponseResultListener<ActivityDegreeListBean>() {
                    @Override
                    public void success(ActivityDegreeListBean activityDegreeListBean) {
                        adapter.addData(activityDegreeListBean.getRecords());
                    }

                    @Override
                    public void failure(CommonException e) {

                    }
                });
        this.compositeSubscription.add(subscription);


    }

    /**
     * 回复系统重建Activity的状态
     *
     * @param savedInstanceState
     */
    private void recoverState(Bundle savedInstanceState) {
        normalState();
    }

    /**
     * 去扫码激活界面
     */
    private void startScanCode() {
        ScanCodeActivity.openActivity(this);
        this.finish();
    }

    private StartWindowDialogBroadcastReceiver startWindowDialogBroadcastReceiver;

    /**
     * 正常状态，进入Activity
     */
    private void normalState() {
        setContentView(R.layout.activity_launcher_main);
        //startEngineService();
        //MessageHandlerService.startService(this);
        startMusicService();
        RemoteServicesClient.getInstance().init(this);
        RemoteServicesClient.getInstance().startRemoteServices();
        RemoteServicesClient.getInstance().bindRemoteServices();

        executeNetWork();
        initLoaderConfig();
        initView();
        AccountManagerClient client = AccountManagerClient.getInstance();
        client.init(getApplicationContext());
        client.addSubscriberListener(this);

        startWindowDialogBroadcastReceiver = new StartWindowDialogBroadcastReceiver();
        startWindowDialogBroadcastReceiver.register(this);

        packageInstallationBroadcastReceiver = new PackageInstallationBroadcastReceiver();
        packageInstallationBroadcastReceiver.register(this);

        // testActivity();
    }

    /**
     * 开启远程的引擎服务
     */
    private void startEngineService() {
        Intent intent = new Intent("com.zk.server.AllService");
        intent.setPackage("com.zk.server");
        sendBroadcast(intent);
    }


    /**
     * 初始化加载器
     */
    private void initLoaderConfig() {
        Subscription subscription = SQLBriteProvider.getInstance(this.getApplicationContext())
                .getBriteDatabase()
                .createQuery(ContentProviderConfig.TABLE_NAME_COLLECTION_APP, "select * from " + ContentProviderConfig.TABLE_NAME_COLLECTION_APP, new String[]{})
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override
                    public void call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        updatedData(cursor);
                    }
                });
        this.compositeSubscription.add(subscription);
    }

    private void queryInstallPackage() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = SQLBriteProvider.getInstance(getApplicationContext())
                        .getBriteDatabase()
                        .query("select * from " + ContentProviderConfig.TABLE_NAME_COLLECTION_APP, new String[]{});
                subscriber.onNext(cursor);
            }
        }).compose(SubscribeUtils.createTransformer())
                .subscribe(new Action1() {
                               @Override
                               public void call(Object result) {
                                   updatedData((Cursor) result);
                               }
                           }

                );
        this.compositeSubscription.add(subscription);
    }

    private void updatedData(Cursor cursor) {
        Log.i(TAG, " Launcher updatedData ");
        addData(ConversionObjectUtils.conversionEntity(cursor));
    }


    private void addChildView() {
        mainMessageFragment = MainMessageFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.main_change_layout, mainMessageFragment, MainMessageFragment.TAG).commitAllowingStateLoss();
    }

    private TextView userNameTextView;

    private ImageView iconImageView;


    /**
     * 初始化控件
     */
    private void initView() {
        this.userNameTextView = (TextView) findViewById(R.id.launcher_user_name);
        this.iconImageView = (ImageView) findViewById(R.id.launcher_user_icon);
        this.userNameTextView.setText(userInfo.nickName);
        GlideLoader.loadNetWorkResource(this, userInfo.icon, this.iconImageView, true);
        this.horizontalScrollBGView = (HorizontalScrollBGView) findViewById(R.id.launcher_main_horizontal_scroll_bg_view);
        this.horizontalScrollBGView.setImageBitmap(R.mipmap.launcher_home_screen_large_bg);
        this.launcherRotatingMenu = (LauncherRotatingMenu) findViewById(R.id.launcher_main_rotating_menu);
        this.launcherRotatingMenu.setScrollResponseCompact(horizontalScrollBGView);
        launcherRotatingMenu.setCallBack(this);

        this.strokeTextView = (StrokeTextView) findViewById(R.id.launcher_main_stroke_tv);
        this.strokeTextView.setModeStroke(StrokeTextView.MODE_STROKE, Color.parseColor("#973610"), 5);
        this.strokeTextView.setText("30");

        this.baseRecyclerView = (BaseRecyclerView) findViewById(R.id.launcher_main_person_ranking_bottom_recycler_view);
        this.baseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonRankingAdapter();

        this.baseRecyclerView.setAdapter(adapter);

        this.treasureLay = (FrameLayout) findViewById(R.id.launcher_treasure_box_lay);
        this.treasureLay.setOnClickListener(this);
        this.todayDoneBtn = (ImageView) findViewById(R.id.launcher_today_done_img);
        this.todayDoneBtn.setOnClickListener(this);
        this.countClock = (CountClock) findViewById(R.id.launcher_treasure_box_count_clock);
        this.countClock.start(3600);

        this.middleWingsWavingLayout = (WingsWavingLayout) findViewById(R.id.launcher_main_WingsWavingLayout_large);
        this.middleWingsWavingLayout.setOnClickListener(this);
        this.leftWingsWavingLayout = (WingsWavingLayout) findViewById(R.id.launcher_main_WingsWavingLayout_left);
        this.rightWingsWavingLayout = (WingsWavingLayout) findViewById(R.id.launcher_main_WingsWavingLayout_right);
        this.leftWingsWavingLayout.setCurrentMode(WingsWavingLayout.MODE_SMALL);
        this.middleWingsWavingLayout.setCurrentMode(WingsWavingLayout.MODE_LARGE);
        this.rightWingsWavingLayout.setCurrentMode(WingsWavingLayout.MODE_SMALL);


        mSeekBarView = (PercentProgressBar) findViewById(R.id.seek_bar);
        mSeekBarView.setProgress(80);
        le = (LinearLayout) findViewById(R.id.launcher_medal);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(MainActivity.this, 20), DisplayUtils.dip2px(MainActivity.this, 20));
        params.leftMargin = DisplayUtils.dip2px(MainActivity.this, 5);
        for (int i = 1; i <= 6; i++) {
            ImageView imageView = new ImageView(this);
            String url = "";
            url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505205073787&di=f080c96d72e8b8e46386623917fbb1ae&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01470e55429fd40000019ae99bd581.jpg";
            GlideLoader.loadNetWorkResource(MainActivity.this, url, imageView);

            le.addView(imageView, params);
        }
        findViewById(R.id.launcher_user_icon)
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            againLoginDialog = createDialog();
                                            againLoginDialog.show();
                                        }
                                    }
                );
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注销登入");
        builder.setMessage("注销将，重新扫码激活");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAgainLoginDialog();
                logOut();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAgainLoginDialog();
            }
        });
        return builder.create();
    }

    /**
     * 注销
     */
    private void logOut() {
        baseManager.deleteAccount(userInfo.userId, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                ScanCodeActivity.openActivity(MainActivity.this);
                MainActivity.this.finish();

            }

            @Override
            public void onError(Throwable e) {
                ScanCodeActivity.openActivity(MainActivity.this);
                MainActivity.this.finish();
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }

    private AlertDialog againLoginDialog;

    private Bitmap bgBitmap;

    private Bitmap loadChildMenuBG() {
        if (bgBitmap == null) {
            bgBitmap = BitmapUtils.decodeBitmapResource(getApplicationContext(), R.drawable.menu_child_circle_bg);
        }
        return bgBitmap;
    }


    /**
     * 添加到
     *
     * @param list
     */
    private void addData(final List<LauncherRotatingMenu.TestEntity> list) {
        subscription = Observable.create(new Observable.OnSubscribe<List<LauncherRotatingMenu.TestEntity>>() {
            @Override
            public void call(Subscriber<? super List< LauncherRotatingMenu.TestEntity>> subscriber) {
                List<LauncherRotatingMenu.TestEntity> testEntityList = new ArrayList<>();
                Log.i(TAG, " 查询到收藏应用程序的个数： " + list.size());
                for (LauncherRotatingMenu.TestEntity testEntity : list) {
                    Bitmap icon_bitmap = AppFilterUtil.filterApp(BaseApplication.getInstance(), testEntity.packageName);
                    testEntity.bitmap = BitmapUtils.drawnRoundBitmap(icon_bitmap, loadChildMenuBG());
                }
                testEntityList.addAll(0, list);
                testEntityList.addAll(list.size(), LauncherRotatingMenu.createTestData(list.size() + 1));
                subscriber.onNext(testEntityList);
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LauncherRotatingMenu.TestEntity>>() {
                               @Override
                               public void call(List<LauncherRotatingMenu.TestEntity> testEntities) {
                                   launcherRotatingMenu.setLocalBitmapSize(16 - list.size());
                                   launcherRotatingMenu.addData((List<LauncherRotatingMenu.TestEntity>) testEntities);
                               }
                           }
                );
        this.compositeSubscription.add(subscription);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.SET_PREFERRED_APPLICATIONS_CODE:
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {//已经授权
                    LauncherUtils.instance.setDefaultLauncher(BaseApplication.getInstance(), this.getClass().getSimpleName());
                } else {//未授权
                    ToastUtils.showToast(getApplicationContext(), "请开启设置Launcher权限");
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SuspensionWindowManagerUtils.request_code_window:
                if (SuspensionWindowManagerUtils.checkPermission(this)) {
                    SuspensionWindowManagerUtils.openSuspensionWindow(this);
                } else {
                    Toast.makeText(getApplicationContext(), "弹窗权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        SystemUIManager.setStickyStyle(getWindow());
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launcher_treasure_box_lay:
                if (countClock.isDoneMark()) {
                    // 宝箱点击
                    OpenBoxDialog dialog = new OpenBoxDialog(this);
                    dialog.addView(R.drawable.open_box_bg4);
                    dialog.addView(R.drawable.open_box_bg4);
                    dialog.show();
                } else {
                    ToastUtils.showToast(this.getApplication(), "活动未到时间");
                }
                break;
            case R.id.launcher_today_done_img:
//                Intent intent1 = new Intent();
//                intent1.setClassName("com.zhongke.content", "com.zhongke.content.activity.TodayAchievementActivity");
//                if (intent1.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent1);
//                }
                if (dialog != null) {
                    dialog.show();
                }
                // 今日成就点击
                break;
            case R.id.launcher_main_WingsWavingLayout_large:
                Intent intent = new Intent();
                intent.setClassName("com.zhongke.content", "com.zhongke.content.activity.DesireActivity");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "未安装程序", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (musicService != null) {
            musicService.stop();
        }
        cancelObservable();
    }

    /**
     * 取消订阅
     */
    private void cancelObservable() {
        if (subscription != null) {
            this.compositeSubscription.remove(subscription);
            this.subscription = null;
        }
    }

    private boolean isAgainLoad = false;
    private PackageInstallationBroadcastReceiver packageInstallationBroadcastReceiver;

    private class PackageInstallationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.isAgainLoad = true;
        }

        public void register(Context context) {
            IntentFilter intentFilter = new IntentFilter("com.zongke.hapilolauncher.homeScreen.PackageInstallationBroadcastReceiver");
            context.registerReceiver(this, intentFilter);
        }

        public void unRegister(Context context) {
            context.unregisterReceiver(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicService != null) {
            if (!musicService.isPlaying()) {
                musicService.play();
            }
        }
        if (isAgainLoad) {
            isAgainLoad = false;
            queryInstallPackage();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountManagerClient.getInstance().removeSubscriberListener(this);
        getSupportLoaderManager().destroyLoader(loaderId);
        releaseResources();
        AccountManagerClient.getInstance().unInit();
        cancelAgainLoginDialog();

    }

    private void cancelAgainLoginDialog() {
        if (againLoginDialog != null && againLoginDialog.isShowing()) {
            againLoginDialog.dismiss();
        }
    }

    /**
     * 释放资源，例如：
     * 1. 关闭dialog防止指针弱引用
     * 2. 释放后台服务，反注册广播等
     */
    private void releaseResources() {
        if (countClock != null) {
            this.countClock.stop();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        stopMusicService();
        if (bgBitmap != null) {
            bgBitmap.recycle();
            bgBitmap = null;
        }
        if (startWindowDialogBroadcastReceiver != null) {
            startWindowDialogBroadcastReceiver.unRegister(this);
        }
        if (packageInstallationBroadcastReceiver != null) {
            packageInstallationBroadcastReceiver.unRegister(this);
        }
    }


    /**
     * 开始背景音乐服务
     */
    public void startMusicService() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    /**
     * 停止背景音乐服务
     */
    public void stopMusicService() {
        if (musicService != null) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = new RushWorkDialog(this);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == loaderId) {
            return new android.support.v4.content.CursorLoader(this, ContentProviderConfig.URI_COLLECTION_APP, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == loaderId) {
            addData(ConversionObjectUtils.conversionEntity(data));
        }


    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    /**
     * 开启
     *
     * @param context
     */
    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    public void accountChange(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return;
        }
        userNameTextView.setText(accountInfo.nickName);
        GlideLoader.loadNetWorkResource(this, accountInfo.icon, iconImageView);
    }

    @Override
    public void accountDelete() {

    }

    @Override
    public void chatMessageResponse(ZkLocalMessage zkLocalMessage) {

    }

    @Override
    public void extraMessageResponse(ZkLocalMessage zkLocalMessage) {

    }

    @Override
    public void netChangeResult(boolean connectResult) {

    }
}
