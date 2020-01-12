package com.goldze.mvvmhabit.ui.main;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppApplication;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityDevicelistBinding;
import com.goldze.mvvmhabit.ui.viewpager.adapter.DeviceListBindingAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class DeviceListActivity extends BaseActivity<ActivityDevicelistBinding, DeviceListViewModel> {

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
        viewModel.setContext(this);
        binding.setAdapter(new DeviceListBindingAdapter());
        viewModel.initToolbar();
        requestBlutoothScanPermissions();
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
                            ToastUtils.showShort("设备扫描限已经打开");
                            //扫描蓝牙设备数据
                            viewModel.requestDeviceList();
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
    }
}
