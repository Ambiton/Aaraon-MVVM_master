package com.goldze.mvvmhabit.ui.main;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityDemoBinding;
import com.goldze.mvvmhabit.databinding.ActivityLoadingBinding;
import com.goldze.mvvmhabit.ui.login.LoginActivity;
import com.goldze.mvvmhabit.ui.login.LoginViewModel;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.BleConnectManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

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
    private int GPS_REQUEST_CODE = 1;
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
    public LoadingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LoadingViewModel.class);
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
    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()){
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(viewModel.isNeedLogin(LoadingActivity.this)){
                        startActivity(LoginActivity.class);
                        finish();
                    }else{
                        startActivity(DeviceListActivity.class);
                        finish();
                    }

                }
            },1000);
        }else {
            new AlertDialog.Builder(this).setTitle("为了保证扫描设备过程正常，请打开 GPS")
                    .setMessage("请前往设置打开GPS")
                    //  取消选项
                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(LoadingActivity.this, "close", Toast.LENGTH_SHORT).show();
                            // 关闭dialog
                            dialogInterface.dismiss();
                            openGPSSEtting();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
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
                            openGPSSEtting();

                        } else {
                           // ToastUtils.showShort("权限被拒绝,应用将无法正常使用");
                            requestAppPermissions();
                        }
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==GPS_REQUEST_CODE){
            openGPSSEtting();
        }
    }
}
