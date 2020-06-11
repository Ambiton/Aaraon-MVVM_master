package com.goldze.mvvmhabit.ui.main;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.FragmentAgreementBinding;
import com.goldze.mvvmhabit.databinding.FragmentWebviewBinding;
import com.goldze.mvvmhabit.entity.FormEntity;
import com.tamsiree.rxtool.RxWebViewTool;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.MaterialDialogUtils;

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
