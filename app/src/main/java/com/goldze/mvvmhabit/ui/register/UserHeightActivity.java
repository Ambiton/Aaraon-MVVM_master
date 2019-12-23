package com.goldze.mvvmhabit.ui.register;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityLoginBinding;
import com.goldze.mvvmhabit.databinding.ActivityUserheightEditBinding;
import com.goldze.mvvmhabit.ui.login.LoginViewModel;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:用户昵称编辑界面
 * @date : 2019/12/14 20:25
 */
public class UserHeightActivity extends BaseActivity<ActivityUserheightEditBinding, UserHeightViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_userheight_edit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
    }
}
