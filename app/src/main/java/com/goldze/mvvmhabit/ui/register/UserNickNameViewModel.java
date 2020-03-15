package com.goldze.mvvmhabit.ui.register;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author Areo
 * @description:
 * @date : 2019/12/14 20:25
 */
public class UserNickNameViewModel extends BaseViewModel{


    //登录按钮的点击事件
    public BindingCommand nextButtonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击下一步，进入记录当前的生日信息
            startActivity(SexChooseActivity.class);
        }
    });

    public UserNickNameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
