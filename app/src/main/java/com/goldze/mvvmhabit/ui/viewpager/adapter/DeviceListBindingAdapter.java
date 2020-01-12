package com.goldze.mvvmhabit.ui.viewpager.adapter;

import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.goldze.mvvmhabit.databinding.ItemDevicelistBinding;
import com.goldze.mvvmhabit.databinding.ItemViewpagerBinding;
import com.goldze.mvvmhabit.ui.main.DeviceListItemViewModel;
import com.goldze.mvvmhabit.ui.viewpager.vm.ViewPagerItemViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;

/**
 * Created by goldze on 2018/6/21.
 */

public class DeviceListBindingAdapter extends BindingRecyclerViewAdapter<DeviceListItemViewModel> {

    @Override
    public void onBindBinding(final ViewDataBinding binding, int variableId, int layoutRes, final int position, DeviceListItemViewModel item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
    }

}
