package com.myzr.allproducts.ui.register;

import android.os.Bundle;

import com.myzr.allproducts.BR;
import com.myzr.allproducts.R;
import com.myzr.allproducts.databinding.ActivitySexchooseEditBinding;
import com.myzr.allproducts.entity.http.userinfo.RegisterUserInfoEntity;
import com.myzr.allproducts.utils.AppTools;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * @author Areo
 * @description:用户性别编辑界面
 * @date : 2019/12/14 20:25
 */
public class SexChooseActivity extends BaseActivity<ActivitySexchooseEditBinding, SexChooseViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sexchoose_edit;
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
