package com.myzr.allproducts.ui.main.lisa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;

import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.app.AppApplication;
import com.myzr.allproducts.app.AppViewModelFactory;
import com.myzr.allproducts.databinding.ActivityLisatestBinding;
import com.myzr.allproducts.databinding.ActivityLoadingBinding;
import com.myzr.allproducts.entity.http.checkversion.CheckUpdateResponseDataEntity;
import com.myzr.allproducts.ui.login.LoginActivity;
import com.myzr.allproducts.ui.main.DeviceListActivity;
import com.myzr.allproducts.utils.AppTools;
import com.tamsiree.rxtool.RxLogTool;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by 袁剑
 */


public class LisaActivity extends BaseActivity<ActivityLisatestBinding, LisaViewModel> {
    private static final String TAG="LisaActivity";

    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public LisaViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LisaViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_lisatest;
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
        initObeserverEvent();
    }

    private void initObeserverEvent() {
    }

    private void startJump() {
    }
}
