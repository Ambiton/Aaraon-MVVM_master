package com.myzr.allproducts.ui.register;

import android.app.Application;
import android.os.Bundle;

import com.myzr.allproducts.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproducts.utils.AppTools;

import androidx.databinding.ObservableField;
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
    //用户昵称的绑定
    public ObservableField<String> userNickName = new ObservableField<>("");
    private RegisterUserInfoEntity userInfoEntity=new RegisterUserInfoEntity();

    //登录按钮的点击事件
    public BindingCommand nextButtonClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击下一步，进入记录当前的生日信息
            Bundle bundle=new Bundle();
            userInfoEntity.setUserNickName(userNickName.get());
            bundle.putParcelable(AppTools.KEY_REGISTER_USERINFO,userInfoEntity);
            startActivity(SexChooseActivity.class,bundle);
        }
    });

    public void setUserInfoEntity(RegisterUserInfoEntity userInfoEntity){
        if(userInfoEntity==null){
            return;
        }
        this.userInfoEntity=userInfoEntity;
    }

    public UserNickNameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
