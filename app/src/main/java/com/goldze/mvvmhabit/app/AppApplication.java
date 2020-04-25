package com.goldze.mvvmhabit.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Process;
import android.text.TextUtils;

import com.goldze.mvvmhabit.BuildConfig;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.entity.DaoMaster;
import com.goldze.mvvmhabit.entity.DaoSession;
import com.goldze.mvvmhabit.ui.login.LoginActivity;
import com.goldze.mvvmhabit.utils.BleOption;
import com.inuker.bluetooth.library.BluetoothClient;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxLogTool;
import com.tamsiree.rxtool.RxNetTool;
import com.tamsiree.rxtool.RxProcessTool;
//import com.squareup.leakcanary.LeakCanary;

import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.base.AppManager;
import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.http.interceptor.logging.Logger;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {
    private static final String TAG = "AppApplication";
    private static BluetoothClient mClient;
    private static final String DB_NAME="myzr_db_date";
    private static final int TIME_PERIOD_OBSERVER = 1 * 60 * 1000;//检测进程后台运行的最大时间MAX_OBSERVER_INDEX*60*1000
    private static final int MAX_OBSERVER_INDEX = 10;//检测进程后台运行的最大时间MAX_OBSERVER_INDEX*60*1000
    private static AppApplication sInstance;
    private volatile DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private Timer observerTimer;//检测进程是否在后台运行
    private int observerTimerIndex=0;//检测进程灾后太的时间

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);

        //初始化全局异常崩溃
        initCrash();
        //GreenDao数据库设置
        setDatabase();
        mClient = new BluetoothClient(this);
        startObserverTimer();
        RxLogTool.d(TAG, "app onCreate...");
        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
    }
    /**
     * 获得当前app运行的Application
     */
    public static AppApplication getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }
    private void startObserverTimer(){
        stopObserverTimer();
        observerTimer=new Timer();
        observerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (RxAppTool.getAppPackageName(AppApplication.getInstance()).equals(RxProcessTool.getForegroundProcessName(AppApplication.getInstance()))) {
                    observerTimerIndex = 0;
                    RxLogTool.d(TAG, "app not AppBackground...current forgment process is  " + RxProcessTool.getForegroundProcessName(AppApplication.getInstance()));
                } else {
                    if (observerTimerIndex >= MAX_OBSERVER_INDEX) {
                        stopObserverTimer();
                        mClient.disconnect(BleOption.getInstance().getMac());
                        AppManager.getAppManager().finishAllActivity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        RxLogTool.d(TAG, "app is background too long...kill...");
//                        if(TextUtils.isEmpty(BleOption.getInstance().getMac())){
//
//                        }else{
//                            stopObserverTimer();
//                            mClient.disconnect(BleOption.getInstance().getMac());
//                            AppManager.getAppManager().finishAllActivity();
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            RxLogTool.d(TAG, "app is background too long...kill...");
//                        }

                    }else{
                        observerTimerIndex++;
                        RxLogTool.d(TAG, "app isAppBackground...observerTimerIndex is  " + observerTimerIndex);
                    }

                }

            }
        },1000,TIME_PERIOD_OBSERVER);
    }

    @Override
    public void exitApp() {
        super.exitApp();
        RxLogTool.d(TAG, "exit app... ");
        stopObserverTimer();
        mClient.disconnect(BleOption.getInstance().getMac());
        android.os.Process.killProcess(android.os.Process.myPid());

    }
    private void stopObserverTimer(){
        if(observerTimer!=null){
            observerTimer.cancel();
            observerTimer=null;
        }
    }
    public static synchronized BluetoothClient getBluetoothClient(Context context) {
        if (mClient == null) {
            mClient = new BluetoothClient(context.getApplicationContext());
        }
        return mClient;
    }

    /**
     * 设置greenDAO
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

        mHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，unitId所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}
