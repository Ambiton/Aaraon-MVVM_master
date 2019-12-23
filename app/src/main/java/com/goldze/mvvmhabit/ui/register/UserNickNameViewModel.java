package com.goldze.mvvmhabit.ui.register;

import android.app.Application;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.ui.main.DemoActivity;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

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
