package com.goldze.mvvmhabit.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.databinding.ActivityLoginBinding;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

/**
 * 一个MVVM模式的注册界面
 */
public class RegisterActivity extends BaseActivity<ActivityLoginBinding, RegisterViewModel> {
    private static final String TAG="RegisterActivity";
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RegisterViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.dialogEvent.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                MaterialDialogUtils.showBasicDialogNoCancel(RegisterActivity.this,viewModel.dialogEvent.get()).show();
            }
        });
    }

}
