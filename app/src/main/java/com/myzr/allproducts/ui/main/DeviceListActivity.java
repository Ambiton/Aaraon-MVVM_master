package com.myzr.allproducts.ui.main;

import android.Manifest;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.app.AppViewModelFactory;
import com.myzr.allproducts.databinding.ActivityDevicelistBinding;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.myzr.allproducts.ui.viewpager.adapter.DeviceListBindingAdapter;
import com.myzr.allproducts.utils.AppTools;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.tamsiree.rxtool.RxLogTool;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Timer;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class DeviceListActivity extends BaseActivity<ActivityDevicelistBinding, DeviceListViewModel> {
    private static final String TAG="DeviceListActivity";
    private int GPS_REQUEST_CODE = 1;
    private Timer timer=new Timer();
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_devicelist;
    }

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public DeviceListViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用DeviceListViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(DeviceListViewModel.class);
    }


    @Override
    public void initData() {
        //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象。
        // Adapter属于View层的东西, 不建议定义到ViewModel中绑定，以免内存泄漏
        viewModel.setActivity(this);
        binding.setAdapter(new DeviceListBindingAdapter());
        binding.include.ivRightIcon.setImageResource(R.mipmap.roundlogo);
        binding.include.ivRightIcon.setVisibility(View.VISIBLE);
        viewModel.initToolbar();
        viewModel.checkVersion();
        AppApplication.getBluetoothClient(getApplication()).registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if(!openOrClosed){
                    ToastUtils.showLong("蓝牙已关闭，为了确保能正常使用设备，请打开手机蓝牙...");
                }
                viewModel.requestDeviceList();
            }
        });

        startRequestTimer();
    }

    private void startRequestTimer(){
//        stopRequestTimer();
//        if(timer==null){
//            timer=new Timer();
//        }
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                viewModel.requestDeviceList();
//            }
//        },1000,8000);
    }

    private void stopRequestTimer(){
//        if(timer!=null){
//            timer.cancel();
//            timer=null;
//        }
    }

    private void openGPSSEtting() {
        if (AppTools.checkGpsIsOpen(this)) {
            viewModel.requestDeviceList();
        } else {
            new AlertDialog.Builder(this).setTitle("为了保证扫描设备过程正常，请打开 GPS")
                    .setMessage("请前往设置打开GPS")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(DeviceListActivity.this, "close", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        dismissDialog();
        requestBlutoothScanPermissions();
        viewModel.submitUserDeviceOption();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            openGPSSEtting();
        }
    }
    /**
     * 请求扫描蓝牙设备需要的权限
     */
    private void requestBlutoothScanPermissions() {
        //请求打开相机权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //ToastUtils.showShort("设备扫描限已经打开");
                            //扫描蓝牙设备数据
                            openGPSSEtting();
                        } else {
                            finish();
                        }
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.cancelScan();
        AppApplication.getInstance().exitApp();
    }


    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
             //   binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
             //  binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
        //监听删除条目
        viewModel.deleteItemLiveData.observe(this, new Observer<DeviceListItemViewModel>() {
            @Override
            public void onChanged(@Nullable final DeviceListItemViewModel deviceListItemViewModel) {
                int index = viewModel.getItemPosition(deviceListItemViewModel);
                //删除选择对话框
                MaterialDialogUtils.showBasicDialog(DeviceListActivity.this, "提示", "是否删除【" + deviceListItemViewModel.entity.get().getDeviceName() + "】？ position：" + index)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ToastUtils.showShort("取消");
                            }
                        }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        viewModel.deleteItem(deviceListItemViewModel);
                    }
                }).show();
            }
        });

        viewModel.versionEvent.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.versionEvent.get()!=null&&viewModel.versionEvent.get().getResponseDataEntityArray()!=null
                        &&viewModel.versionEvent.get().getResponseDataEntityArray().length>0){
                    for(CheckUpdateResponseDataEntity checkUpdateResponseDataEntity:viewModel.versionEvent.get().getResponseDataEntityArray()){
                        if(CheckUpdateResponseDataEntity.TYPE_APP.equals(checkUpdateResponseDataEntity.getType())){
                            final CheckUpdateResponseDataEntity appCheckUpdateResponseDataEntity=checkUpdateResponseDataEntity;
                            if(AppTools.isNeedUpdate(DeviceListActivity.this,appCheckUpdateResponseDataEntity.getNewestVerno())){
                                MaterialDialog.Builder builder=MaterialDialogUtils.showUpdateDialog(DeviceListActivity.this,appCheckUpdateResponseDataEntity.getDesc(),CheckUpdateResponseDataEntity.FORCE_UPGRADE.equals(appCheckUpdateResponseDataEntity));
                                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        AppTools.downFile(DeviceListActivity.this,appCheckUpdateResponseDataEntity.getPackSavepath(),null);
                                    }
                                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    }
                                }).show();

                            }
                            break;
                        }else if (CheckUpdateResponseDataEntity.TYPE_BANNER.equals(checkUpdateResponseDataEntity.getType())) {
                            viewModel.setBannerPlayModel(checkUpdateResponseDataEntity.getPlayMode());
                            //Banner 版本检测更新
                            if(!AppTools.isBannerUnZipFilesNormal(DeviceListActivity.this)||AppTools.isVersionNeedUpdate(viewModel.getBannerVersion(),checkUpdateResponseDataEntity.getNewestVerno())){
                                RxLogTool.e("TAG","banner need update...");
                                AppTools.downImageBannerFiles(DeviceListActivity.this, checkUpdateResponseDataEntity, viewModel);
                            }

                        } else if (CheckUpdateResponseDataEntity.TYPE_LOAD_RES.equals(checkUpdateResponseDataEntity.getType())) {
                            //Loading图 版本检测更新
                            if(AppTools.isVersionNeedUpdate(viewModel.getLoadingVersion(),checkUpdateResponseDataEntity.getNewestVerno())){
                                RxLogTool.e("TAG","loadImage need update...");
                                AppTools.downImageLoadingFiles(DeviceListActivity.this, checkUpdateResponseDataEntity,viewModel);
                            }
                        } else {
                            RxLogTool.e(TAG,"other version retrun...");
                        }
                    }
                }else{
                    RxLogTool.e(TAG,"no version retrun...");
                }

            }
        });
    }
}
