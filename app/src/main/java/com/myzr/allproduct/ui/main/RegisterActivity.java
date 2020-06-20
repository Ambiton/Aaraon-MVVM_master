package com.myzr.allproduct.ui.main;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;

import com.myzr.allproduct.BR;
import com.myzr.allproduct.R;
import com.myzr.allproduct.app.AppViewModelFactory;
import com.myzr.allproduct.databinding.ActivityRegisternewBinding;
import com.tamsiree.rxtool.RxTextTool;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

/**
 * 一个MVVM模式的注册界面
 */
public class RegisterActivity extends BaseActivity<ActivityRegisternewBinding, RegisterViewModel> {
    private static final String TAG="RegisterActivity";
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_registernew;
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
        RxTextTool.getBuilder("").setUnderline();
        binding.cbRegister.setOnCheckedChangeListener(viewModel);
        viewModel.dialogEvent.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                MaterialDialogUtils.showBasicDialogNoCancel(RegisterActivity.this,viewModel.dialogEvent.get()).show();
            }
        });
    }

}
