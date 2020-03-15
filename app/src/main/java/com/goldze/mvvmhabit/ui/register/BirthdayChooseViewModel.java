package com.goldze.mvvmhabit.ui.register;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;

import android.view.View;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * @author Areo
 * @description:
 * @date : 2019/12/14 20:25
 */
public class BirthdayChooseViewModel extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public BirthdayChooseViewModel.UIChangeObservable uc = new BirthdayChooseViewModel.UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public BirthdayChooseViewModel(@NonNull Application application) {
        super(application);
        //从本地取得数据绑定到View层
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
        }
    });
    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });
    //登录按钮的点击事件
    public BindingCommand nextButtonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击下一步，进入记录体重的生日信息
            startActivity(UserWeightActivity.class);

        }
    });

    //用户协议的点击事件
    public BindingCommand dateChangeLister = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //当前选择的日期
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
