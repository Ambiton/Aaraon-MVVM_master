package com.goldze.mvvmhabit.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.databinding.ActivityDemoBinding;
import com.goldze.mvvmhabit.databinding.ActivityLoadingBinding;
import com.goldze.mvvmhabit.ui.login.LoginActivity;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.BleConnectManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.ResponseBody;

/**
 * Created by 原件
 */


public class LoadingActivity extends BaseActivity<ActivityLoadingBinding, LoadingViewModel> {

    private String[] permissions = {//权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_loading;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
    }

    @Override
    public void initData() {
        super.initData();
        requestAppPermissions();
    }

    /**
     * 请求相机权限
     */
    private void requestAppPermissions() {
        //请求APP需要的权限
        RxPermissions rxPermissions = new RxPermissions(LoadingActivity.this);
        rxPermissions.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            AppApplication.getBluetoothClient(LoadingActivity.this).openBluetooth();
                           new Handler(getMainLooper()).postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   startActivity(LoginActivity.class);
                                   finish();
                               }
                           },2000);

                        } else {
                           // ToastUtils.showShort("权限被拒绝,应用将无法正常使用");
                            requestAppPermissions();
                        }
                    }
                });
    }

    private void downFile(String url) {
        String destFileDir = getApplication().getCacheDir().getPath();
        String destFileName = System.currentTimeMillis() + ".apk";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DownLoadManager.getInstance().load(url, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                ToastUtils.showShort("文件下载完成！");
            }

            @Override
            public void progress(final long progress, final long total) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) progress);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort("文件下载失败！");
                progressDialog.dismiss();
            }
        });
    }
}
