package com.myzr.allproduct.ui.register;

import android.os.Bundle;
import com.myzr.allproduct.BR;
import com.myzr.allproduct.R;
import com.myzr.allproduct.databinding.ActivityUsernicknameEditBinding;
import com.myzr.allproduct.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproduct.utils.AppTools;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:用户昵称编辑界面
 * @date : 2019/12/14 20:25
 */
public class UserNickNameActivity extends BaseActivity<ActivityUsernicknameEditBinding, UserNickNameViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_usernickname_edit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            viewModel.setUserInfoEntity(null);
        }else{
            RegisterUserInfoEntity userInfoEntity=bundle.getParcelable(AppTools.KEY_REGISTER_USERINFO);
            viewModel.setUserInfoEntity(userInfoEntity);
        }
    }

    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
    }
}
