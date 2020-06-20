package com.myzr.allproduct.ui.main;

import android.os.Bundle;

import com.myzr.allproduct.BR;
import com.myzr.allproduct.R;
import com.myzr.allproduct.databinding.FragmentAgreementBinding;
import com.myzr.allproduct.databinding.FragmentWebviewBinding;
import com.tamsiree.rxtool.RxWebViewTool;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * Created by goldze on 2017/7/17.
 * 表单提交/编辑界面
 */

public class WebViewActivity extends BaseActivity<FragmentWebviewBinding, WebViewViewModel> {

    public static final String KEY_URI="webview_url";
    @Override
    public void initParam() {
        //获取列表传入的实体
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //通过binding拿到toolbar控件, 设置给Activity
      setSupportActionBar(binding.include.toolbar);
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            RxWebViewTool.initWebView(this,binding.webview);
            binding.webview.loadUrl(bundle.getString(KEY_URI));
        }
        //初始化标题
        viewModel.initToolbar();
    }

    @Override
    public void initViewObservable() {
    }
}
