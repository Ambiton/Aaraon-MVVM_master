package com.myzr.allproduct.ui.main;

import android.app.Application;
import android.view.View;

import com.myzr.allproduct.R;
import com.myzr.allproduct.ui.base.viewmodel.ToolbarViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class WebViewViewModel extends ToolbarViewModel {
    public SingleLiveEvent<String> entityJsonLiveData = new SingleLiveEvent<>();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc;

    public class UIChangeObservable {
        //显示日期对话框
        public ObservableBoolean showDateDialogObservable;

        public UIChangeObservable() {
            showDateDialogObservable = new ObservableBoolean(false);
        }
    }

    public WebViewViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uc = new UIChangeObservable();
        //sexItemDatas 一般可以从本地Sqlite数据库中取出数据字典对象集合，让该对象实现IKeyAndValue接口
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        //初始化标题栏
        setRightTextVisible(View.GONE);
        setTitleText(getApplication().getString(R.string.user_ad_title));
    }

    @Override
    public void rightTextOnClick() {
        ToastUtils.showShort("更多");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
