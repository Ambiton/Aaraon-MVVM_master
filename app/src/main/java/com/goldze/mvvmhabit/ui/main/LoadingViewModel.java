package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.goldze.mvvmhabit.data.DemoRepository;
import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.form.FormFragment;
import com.goldze.mvvmhabit.ui.network.NetWorkFragment;
import com.goldze.mvvmhabit.ui.rv_multi.MultiRecycleViewFragment;
import com.goldze.mvvmhabit.ui.tab_bar.activity.TabBarActivity;
import com.goldze.mvvmhabit.ui.viewpager.activity.ViewPagerActivity;
import com.goldze.mvvmhabit.ui.vp_frg.ViewPagerGroupFragment;
import com.goldze.mvvmhabit.utils.HttpsUtils;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * Created by goldze on 2017/7/17.
 */

public class LoadingViewModel extends BaseViewModel <DemoRepository>{
    public LoadingViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    public boolean isNeedLogin(Context context){
        return TextUtils.isEmpty(model.getToken())&& NetworkUtil.isNetworkAvailable(context) ;
    }
}
