package com.goldze.mvvmhabit.ui.main;

import android.Manifest;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityLoadingBinding;
import com.goldze.mvvmhabit.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.goldze.mvvmhabit.ui.login.LoginActivity;
import com.goldze.mvvmhabit.ui.register.BirthdayChooseActivity;
import com.goldze.mvvmhabit.ui.register.UserHeightActivity;
import com.goldze.mvvmhabit.ui.register.UserWeightActivity;
import com.goldze.mvvmhabit.utils.AppTools;
import com.tamsiree.rxtool.RxLogTool;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by 袁剑
 */


public class LoadingActivity extends BaseActivity<ActivityLoadingBinding, LoadingViewModel> {
    private static final String TAG="LoadingActivity";
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
        initObeserverEvent();
    }

    private void initObeserverEvent() {
        viewModel.versionEvent.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.versionEvent.get() != null && viewModel.versionEvent.get().getResponseDataEntityArray() != null
                        && viewModel.versionEvent.get().getResponseDataEntityArray().length >= 3) {
//                    MaterialDialog.Builder builderLoading = MaterialDialogUtils.showUpdateResProgressDialog(LoadingActivity.this, false);
                    for (CheckUpdateResponseDataEntity checkUpdateResponseDataEntity : viewModel.versionEvent.get().getResponseDataEntityArray()) {
                        if (CheckUpdateResponseDataEntity.TYPE_APP.equals(checkUpdateResponseDataEntity.getType())) {
                            //APK 版本检测更新
                            final CheckUpdateResponseDataEntity appCheckUpdateResponseDataEntity = checkUpdateResponseDataEntity;
                            if (AppTools.isNeedUpdate(LoadingActivity.this, appCheckUpdateResponseDataEntity.getNewestVerno())) {
                                RxLogTool.e("TAG","apk need update...");
                            }else{
                                viewModel.setApkNewst(true);
                            }
                            break;
                        } else if (CheckUpdateResponseDataEntity.TYPE_BANNER.equals(checkUpdateResponseDataEntity.getType())) {
                            viewModel.setBannerPlayModel(checkUpdateResponseDataEntity.getPlayMode());
                            //Banner 版本检测更新
                            if(AppTools.isVersionNeedUpdate(viewModel.getBannerVersion(),checkUpdateResponseDataEntity.getNewestVerno())){
                                RxLogTool.e("TAG","banner need update...");
                                AppTools.downImageBannerFiles(LoadingActivity.this, checkUpdateResponseDataEntity, viewModel);
                            }else{
                                viewModel.setBannerNewst(true);
                            }

                        } else if (CheckUpdateResponseDataEntity.TYPE_LOAD_RES.equals(checkUpdateResponseDataEntity.getType())) {
                            //Loading图 版本检测更新
                            if(AppTools.isVersionNeedUpdate(viewModel.getBannerVersion(),checkUpdateResponseDataEntity.getNewestVerno())){
                                RxLogTool.e("TAG","loadImage need update...");
                                AppTools.downImageLoadingFiles(LoadingActivity.this, checkUpdateResponseDataEntity,viewModel);
                            }else{
                                viewModel.setLoadNewst(true);
                            }
                        } else {
                            RxLogTool.e(TAG,"other version retrun...");
                        }
                    }


                }

            }
        });

        viewModel.apkNewst.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isAllNewst()) {
                    startJump();
                }
            }
        });
        viewModel.loadNewst.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isAllNewst()) {
                    startJump();
                }
            }
        });
        viewModel.bannerNewst.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isAllNewst()) {
                    startJump();
                }
            }
        });
    }

    private void startJump() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(UserWeightActivity.class);
                if (!viewModel.isNeedLogin(LoadingActivity.this) ) {
                    startActivity(DeviceListActivity.class);
                    finish();
                } else {
                    startActivity(LoginActivity.class);
                    finish();
                }

            }
        }, 2000);
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            RxLogTool.e("Yuanjian",this.getExternalCacheDir().getPath());
            if (viewModel.isNeedCheckUpdate(LoadingActivity.this)) {
                RxLogTool.e("Yuanjian","here");
                viewModel.checkVersion();
            } else {
                startJump();
            }

        } else {
            new AlertDialog.Builder(this).setTitle("为了保证扫描设备过程正常，请打开 GPS")
                    .setMessage("请前往设置打开GPS")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
                            startActivityForResult(intent, GPS_REQUEST_CODE);
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            openGPSSEtting();
        }
    }
}
