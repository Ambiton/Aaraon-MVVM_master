package com.goldze.mvvmhabit.ui.register;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;

import com.goldze.mvvmhabit.entity.http.userinfo.RegisterUserInfoEntity;
import com.goldze.mvvmhabit.utils.AppTools;

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
public class SexChooseViewModel extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public SexChooseViewModel.UIChangeObservable uc = new SexChooseViewModel.UIChangeObservable();

    private RegisterUserInfoEntity userInfoEntity=new RegisterUserInfoEntity();
    public void setUserInfoEntity(RegisterUserInfoEntity userInfoEntity){
        if(userInfoEntity==null){
            return;
        }
        this.userInfoEntity=userInfoEntity;
    }
    public RegisterUserInfoEntity getUserInfoEntity(){
        return this.userInfoEntity;
    }
    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public SexChooseViewModel(@NonNull Application application) {
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

    public BindingCommand femaleImageClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击的是女士
            //点击下一步，进入记录的生日信息
            Bundle bundle=new Bundle();
            userInfoEntity.setSex(RegisterUserInfoEntity.SEX_FEMALE);
            bundle.putParcelable(AppTools.KEY_REGISTER_USERINFO,userInfoEntity);
            startActivity(BirthdayChooseActivity.class,bundle);
        }
    });

    public BindingCommand maleImageClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击的是男士
            //点击下一步，进入记录体重的生日信息
            Bundle bundle=new Bundle();
            userInfoEntity.setSex(RegisterUserInfoEntity.SEX_MALE);
            bundle.putParcelable(AppTools.KEY_REGISTER_USERINFO,userInfoEntity);
            startActivity(BirthdayChooseActivity.class,bundle);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
