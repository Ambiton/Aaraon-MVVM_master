package com.goldze.mvvmhabit.ui.viewpager.adapter;

import androidx.databinding.ViewDataBinding;

import com.goldze.mvvmhabit.ui.main.DeviceListItemViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by goldze on 2018/6/21.
 */

public class DeviceListBindingAdapter extends BindingRecyclerViewAdapter<DeviceListItemViewModel> {

    @Override
    public void onBindBinding(final ViewDataBinding binding, int variableId, int layoutRes, final int position, DeviceListItemViewModel item) {
        super.onBindBinding(binding, variableId, layoutRes, position, item);
    }

}
