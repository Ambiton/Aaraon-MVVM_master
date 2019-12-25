package com.goldze.mvvmhabit.ui.main;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.goldze.mvvmhabit.entity.FormEntity;
import com.goldze.mvvmhabit.ui.form.FormFragment;
import com.goldze.mvvmhabit.ui.network.NetWorkFragment;
import com.goldze.mvvmhabit.ui.rv_multi.MultiRecycleViewFragment;
import com.goldze.mvvmhabit.ui.tab_bar.activity.TabBarActivity;
import com.goldze.mvvmhabit.ui.viewpager.activity.ViewPagerActivity;
import com.goldze.mvvmhabit.ui.vp_frg.ViewPagerGroupFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by goldze on 2017/7/17.
 */

public class LoadingViewModel extends BaseViewModel {
    public LoadingViewModel(@NonNull Application application) {
        super(application);
    }
}
